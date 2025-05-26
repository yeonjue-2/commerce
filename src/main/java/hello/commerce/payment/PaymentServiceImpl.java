package hello.commerce.payment;

import hello.commerce.common.model.BusinessException;
import hello.commerce.common.model.ErrorCode;
import hello.commerce.common.properties.KakaoPayProperties;
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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final OrderRepository orderRepository;
    private final WebClient webClient;
    private final KakaoPayProperties kakaoPayProps;
    private final PaymentRepository paymentRepository;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Transactional
    @Override
    public KakaoPayReadyResponseV1 prepareKakaoPay(Long orderId) {
        // 1. 유효성 검사
        Order order = validateOrderCondition(orderId);

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
        payment.setPaidAt(response.getApproved_at());
        payment.setPgToken(pgToken);
        paymentSave(payment);
    }


    private Order validateOrderCondition(Long orderId) {
        // order 데이터
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ORDER));

        // OrderStatus 상태 검증
        if (order.getOrderStatus() != OrderStatus.INITIAL) {
            throw new BusinessException(ErrorCode.INVALID_ORDER_STATUS_TRANSITION);
        }
        return order;
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

    private static Payment createPayment(Order order, KakaoPayReadyResponseV1 response) {
        return Payment.builder()
                .order(order)
                .paymentMethod("KAKAOPAY")
                .paymentStatus(PaymentStatus.INITIAL)
                .totalAmount(order.getTotalAmount())
                .transactionId(response.getTid())
                .isTest(true) // 또는 yml에서 분기
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
        return webClient.post()
                .uri(kakaoPayProps.getReadyUrl()) // /v1/payment/ready
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(requestBody)
                .retrieve()
                .onStatus(
                        status -> status.isError(),
                        clientResponse -> clientResponse.bodyToMono(String.class)
                                .flatMap(errorBody -> {
                                    log.error("카카오페이 오류 응답 본문: {}", errorBody);
                                    return Mono.error(new RuntimeException("카카오페이 API 오류: " + errorBody));
                                })
                )
                .bodyToMono(KakaoPayReadyResponseV1.class)
                .block(); // 동기
    }

    private Payment getValidPayment(Long orderId) {
        return paymentRepository.findByOrderId(orderId)
                .filter(p -> StringUtils.hasText(p.getTransactionId()))
                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_PAYMENT));
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
                .bodyToMono(KakaoPayApproveResponseV1.class)
                .block();
        return response;
    }

    private void paymentSave(Payment payment) {
        paymentRepository.save(payment);
        paymentHistoryRepository.save(new PaymentHistory(payment));
    }
}
