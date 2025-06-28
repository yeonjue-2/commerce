package hello.commerce.payment;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.common.properties.KakaoPayProperties;
import hello.commerce.order.OrderReader;
import hello.commerce.order.OrderRepository;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import hello.commerce.payment.dto.KakaoPayApproveResponseV1;
import hello.commerce.payment.dto.KakaoPayReadyRequestV1;
import hello.commerce.payment.dto.KakaoPayReadyResponseV1;
import hello.commerce.payment.model.Payment;
import hello.commerce.payment.model.PaymentHistory;
import hello.commerce.payment.model.PaymentStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final OrderReader orderReader;
    private final WebClient webClient;
    private final KakaoPayProperties kakaoPayProps;
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Transactional
    @Override
    public KakaoPayReadyResponseV1 prepareKakaoPay(Long orderId) {
        // 1. 유효성 검사 및 중복 방지
        Order order = validateOrderCondition(orderId);
        validateNoExistingPayment(orderId);

        // 2. 카카오페이 요청
        KakaoPayReadyRequestV1 kakaoRequest = createKakaoPayReadyRequest(orderId, order);
        KakaoPayReadyResponseV1 response = callKakaoPayReady(kakaoRequest);

        // 3. 결제 정보 저장
        Payment payment = createPayment(order, response);
        paymentSave(payment);

        return response;
    }

    @Transactional
    @Override
    public void approveKakaoPay(Long orderId, String pgToken) {
        // 1. 유효성 검사
        Order order = validateOrderCondition(orderId);

        // 2. 결제 준비 시 저장해둔 TID 조회
        Payment payment = getValidPayment(orderId);
        String tid = payment.getTransactionId();

        // 3. 카카오페이에 결제 승인 요청
        KakaoPayApproveResponseV1 response = callKakaoPayApprove(pgToken, tid, order);

        // 4. 결제 성공 처리
        order.setOrderStatus(OrderStatus.PAID);
        orderRepository.save(order);

        payment.setPaymentStatus(PaymentStatus.PAID);
        payment.setPaidAt(response.getApprovedAt());
        payment.setPgToken(pgToken);
        paymentSave(payment);
    }


    private Order validateOrderCondition(Long orderId) {
        // order 데이터
        Order order = orderReader.findByIdForUpdate(orderId);

        // OrderStatus 상태 검증
        if (order.getOrderStatus() != OrderStatus.INITIAL) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS_TRANSITION);
        }

        return order;
    }

    private void validateNoExistingPayment(Long orderId) {
        paymentRepository.findByOrderId(orderId)
                .ifPresent(p -> { throw new BusinessException(ErrorCode.ALREADY_PREPARED_PAYMENT); });
    }

    private KakaoPayReadyRequestV1 createKakaoPayReadyRequest(Long orderId, Order order) {
        return KakaoPayReadyRequestV1.builder()
                .cid(kakaoPayProps.getCid())
                .partnerOrderId(order.getId().toString())
                .partnerUserId(order.getUserId().toString())
                .itemName(order.getProduct().getName())
                .quantity(order.getQuantity())
                .totalAmount(order.getTotalAmount())
                .taxFreeAmount(kakaoPayProps.getTaxFreeAmount())
                .approvalUrl(kakaoPayProps.getApprovalRedirectUrl(orderId))
                .cancelUrl(kakaoPayProps.getCancelRedirectUrl(orderId))
                .failUrl(kakaoPayProps.getFailRedirectUrl(orderId))
                .build();
    }

    private KakaoPayReadyResponseV1 callKakaoPayReady(KakaoPayReadyRequestV1 request) {
        Map<Object, Object> requestBody = new HashMap<>();
        requestBody.put("cid", request.getCid());
        requestBody.put("partner_order_id", request.getPartnerOrderId());
        requestBody.put("partner_user_id", request.getPartnerUserId());
        requestBody.put("item_name", request.getItemName());
        requestBody.put("quantity", String.valueOf(request.getQuantity()));
        requestBody.put("total_amount", String.valueOf(request.getTotalAmount()));
        requestBody.put("tax_free_amount", String.valueOf(request.getTaxFreeAmount()));
        requestBody.put("approval_url", request.getApprovalUrl());
        requestBody.put("cancel_url", request.getCancelUrl());
        requestBody.put("fail_url", request.getFailUrl());

        // 응답 객체는 KakaoPayReadyResponseV1와 동일 구조를 맞춰야 함
        KakaoPayReadyResponseV1 response = webClient.post()
                .uri(kakaoPayProps.getReadyUrl()) // /v1/payment/ready
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("카카오페이 오류 응답 본문: {}", errorBody);
                                    return Mono.error(new BusinessException(ErrorCode.KAKAO_API_ERROR));
                                })
                )
                .bodyToMono(KakaoPayReadyResponseV1.class)
                .block();// 동기

        // 응답 값 유효성 검사
        if (response == null || response.getTid() == null || response.getNextRedirectPcUrl() == null) {
            log.error("카카오페이 결제 요청 응답 필드가 누락됨: {}", response);
            throw new BusinessException(ErrorCode.KAKAO_API_ERROR);
        }

        return response;
    }

    private Payment getValidPayment(Long orderId) {
        Payment payment = paymentRepository.findByOrderIdAndTransactionIdIsNotNull(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PAYMENT));

        if (payment.getPaymentStatus() != PaymentStatus.INITIAL) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS_TRANSITION);
        }

        return payment;
    }

    private KakaoPayApproveResponseV1 callKakaoPayApprove(String pgToken, String tid, Order order) {
        HashMap<String, String> form = new HashMap<>();
        form.put("cid", kakaoPayProps.getCid());
        form.put("tid", tid);
        form.put("partner_order_id", order.getId().toString());
        form.put("partner_user_id", order.getUserId().toString());
        form.put("pg_token", pgToken);

        KakaoPayApproveResponseV1 response = webClient.post()
                .uri(kakaoPayProps.getApproveUrl()) // /v1/payment/approve
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(form)
                .retrieve()
                .onStatus(
                        HttpStatusCode::isError,
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("카카오페이 오류 응답 본문: {}", errorBody);
                                    return Mono.error(new BusinessException(ErrorCode.KAKAO_API_ERROR));
                                })
                )
                .bodyToMono(KakaoPayApproveResponseV1.class)
                .block();

        // 응답 값 유효성 검사
        if (response == null || response.getTid() == null || response.getApprovedAt() == null) {
            log.error("카카오페이 승인 응답 필드가 누락됨: {}", response);
            throw new BusinessException(ErrorCode.KAKAO_API_ERROR);
        }

        return response;
    }

    private Payment createPayment(Order order, KakaoPayReadyResponseV1 response) {
        return Payment.builder()
                .order(order)
                .paymentMethod("KAKAOPAY")
                .paymentStatus(PaymentStatus.INITIAL)
                .totalAmount(order.getTotalAmount())
                .transactionId(response.getTid())
                .isTest(true) // 또는 yml에서 분기
                .build();
    }

    private void paymentSave(Payment payment) {
        paymentRepository.save(payment);
        paymentHistoryRepository.save(new PaymentHistory(payment));
    }
}
