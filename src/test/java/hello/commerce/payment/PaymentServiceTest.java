package hello.commerce.payment;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.common.properties.KakaoPayProperties;
import hello.commerce.order.OrderReader;
import hello.commerce.order.OrderRepository;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import hello.commerce.payment.dto.KakaoPayApproveResponseV1;
import hello.commerce.payment.dto.KakaoPayReadyResponseV1;
import hello.commerce.payment.model.Payment;
import hello.commerce.payment.model.PaymentStatus;
import hello.commerce.product.model.Product;
import hello.commerce.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderReader orderReader;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private PaymentHistoryRepository paymentHistoryRepository;
    @Mock
    private KakaoPayProperties kakaoPayProps;
    @Mock
    private WebClient webClient;
    @Mock
    private WebClient.RequestBodyUriSpec requestBodyUriSpec;
    @Mock
    @SuppressWarnings("rawtypes") // 경고 제거용
    private WebClient.RequestHeadersSpec requestHeadersSpec;
    @Mock
    private WebClient.RequestBodySpec requestBodySpec;
    @Mock
    private WebClient.ResponseSpec responseSpec;

    User user;
    Product product;

    private static final Long ORDER_ID = 1L;

    @BeforeEach
    void setUp() {
        user = new User(100L, "nonamed", "", "test@test.com", "ROLE_USER", "", "");
        product = new Product(100L, "향균 베개 커버", 35000, 200);
    }

    @Test
    @DisplayName("prepareKakaoPay - 결제 준비 요청 성공")
    void prepareKakaoPay_success() {
        // given
        Order order = createValidOrder(ORDER_ID);
        KakaoPayReadyResponseV1 readyResponse = createMockKakaoReadyResponse();

        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        mockPrepareKakaoPayProps();
        mockWebClientChain(readyResponse, KakaoPayReadyResponseV1.class);

        // when
        KakaoPayReadyResponseV1 result = paymentService.prepareKakaoPay(ORDER_ID);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getTid()).isEqualTo("TX1234");
    }

    @Test
    @DisplayName("prepareKakaoPay - 존재하지 않는 주문이면 NOT_FOUND_ORDER 예외 발생")
    void prepareKakaoPay_notFoundOrder() {
        // given
        when(orderReader.findByIdForUpdate(ORDER_ID)).thenThrow(new BusinessException(ErrorCode.NOT_FOUND_ORDER));

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.prepareKakaoPay(ORDER_ID));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_ORDER);
    }


    @Test
    @DisplayName("prepareKakaoPay - 주문 상태가 INITIAL이 아니면 INVALID_ORDER_STATUS_TRANSITION 발생")
    void prepareKakaoPay_invalidOrderStatus() {
        // given
        Order order = createValidOrder(ORDER_ID);
        order.setOrderStatus(OrderStatus.PAID);
        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.prepareKakaoPay(ORDER_ID));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_ORDER_STATUS_TRANSITION);
    }

    @Test
    @DisplayName("prepareKakaoPay - 중복 주문이 생성되지 않도록 확인한다.")
    void prepareKakaoPay_checkDuplicatePayment() {
        // given
        Order order = createValidOrder(ORDER_ID);
        Payment payment = createPayment(order);

        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        when(paymentRepository.findByOrderId(ORDER_ID)).thenReturn(Optional.of(payment));

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.prepareKakaoPay(ORDER_ID));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.ALREADY_PREPARED_PAYMENT);
    }

    @Test
    @DisplayName("prepareKakaoPay - 카카오페이 API 오류 발생 시 BusinessException 반환")
    void prepareKakaoPay_kakaoApiError() {
        // given
        Order order = createValidOrder(ORDER_ID);
        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        mockPrepareKakaoPayProps();

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(Map.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // WebClient에서 오류 응답을 시뮬레이션
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            Function<ClientResponse, Mono<? extends Throwable>> errorHandler = invocation.getArgument(1);
            ClientResponse clientResponse = mock(ClientResponse.class);

            // 오류 본문 시뮬레이션
            when(clientResponse.bodyToMono(String.class)).thenReturn(Mono.just("카카오 오류 응답"));
            // 실제로 Mono.error(...)를 반환해야 .block()에서 예외가 터짐
            when(responseSpec.bodyToMono(KakaoPayReadyResponseV1.class))
                    .thenAnswer(inv -> errorHandler.apply(clientResponse).flatMap(Mono::error)); // 핵심

            return responseSpec;
        });

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.prepareKakaoPay(ORDER_ID));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.KAKAO_API_ERROR);
    }

    @Test
    @DisplayName("카카오페이 응답 객체 자체가 null이면 예외 발생")
    void prepareKakaoPay_shouldThrow_whenResponseIsNull() {
        // given
        Order order = createValidOrder(ORDER_ID);
        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        mockPrepareKakaoPayProps();
        mockWebClientChain(null, KakaoPayReadyResponseV1.class);

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.prepareKakaoPay(ORDER_ID));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.KAKAO_API_ERROR);
    }

    @ParameterizedTest(name = "{index}: 카카오페이 응답의 {0} 필드가 null일 때 예외 발생")
    @MethodSource("invalidKakaoPayReadyResponses")
    void prepareKakaoPay_shouldThrow_whenFieldIsNull(String desc, KakaoPayReadyResponseV1 response) {
        Order order = createValidOrder(ORDER_ID);
        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        mockPrepareKakaoPayProps();
        mockWebClientChain(response, KakaoPayReadyResponseV1.class);

        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.prepareKakaoPay(ORDER_ID));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.KAKAO_API_ERROR);
    }

    static Stream<Arguments> invalidKakaoPayReadyResponses() {
        return Stream.of(
                Arguments.of("tid", KakaoPayReadyResponseV1.builder()
                        .tid(null)
                        .nextRedirectPcUrl("https://url")
                        .createdAt(LocalDateTime.now())
                        .build()),
                Arguments.of("nextRedirectPcUrl", KakaoPayReadyResponseV1.builder()
                        .tid("validTid")
                        .nextRedirectPcUrl(null)
                        .createdAt(LocalDateTime.now())
                        .build())
        );
    }

    @Test
    @DisplayName("approveKakaoPay - 카카오페이 결제 승인 성공")
    void approveKakaoPay_success() {
        // given
        Order order = createValidOrder(ORDER_ID);
        Payment payment = createPayment(order);
        KakaoPayApproveResponseV1 approveResponse = createMockKakaoApproveResponse();

        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        when(paymentRepository.findByOrderIdAndTransactionIdIsNotNull(ORDER_ID)).thenReturn(Optional.of(payment));

        when(kakaoPayProps.getCid()).thenReturn("TC0ONETIME");
        when(kakaoPayProps.getApproveUrl()).thenReturn("https://kapi.kakao.com/v1/payment/approve");  // TO-DO

        mockWebClientChain(approveResponse, KakaoPayApproveResponseV1.class);

        // when
        paymentService.approveKakaoPay(ORDER_ID, "pgToken123");

        // then
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.PAID);
        assertThat(payment.getPgToken()).isEqualTo("pgToken123");
    }


    @Test
    @DisplayName("approveKakaoPay - 존재하지 않는 주문이면 예외 발생")
    void approveKakaoPay_notFoundOrder() {
        when(orderReader.findByIdForUpdate(ORDER_ID)).thenThrow(new BusinessException(ErrorCode.NOT_FOUND_ORDER));

        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.approveKakaoPay(ORDER_ID, "pgToken123"));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_ORDER);
    }

    @Test
    @DisplayName("approveKakaoPay - 결제 정보가 없으면 예외 발생")
    void approveKakaoPay_notFoundPayment() {
        Order order = createValidOrder(ORDER_ID);
        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        when(paymentRepository.findByOrderIdAndTransactionIdIsNotNull(ORDER_ID)).thenReturn(Optional.empty());

        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.approveKakaoPay(ORDER_ID, "pgToken123"));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_PAYMENT);
    }

    @Test
    @DisplayName("approveKakaoPay - 결제 상태가 INITIAL이 아니면 예외 발생")
    void approveKakaoPay_paymentStatusIsNotInitial() {
        Order order = createValidOrder(ORDER_ID);
        Payment payment = Payment.builder()
                .order(order)
                .transactionId("TX1234")
                .paymentStatus(PaymentStatus.PAID)
                .build();

        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        when(paymentRepository.findByOrderIdAndTransactionIdIsNotNull(ORDER_ID)).thenReturn(Optional.of(payment));

        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.approveKakaoPay(ORDER_ID, "pgToken123"));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.INVALID_ORDER_STATUS_TRANSITION);
    }

    @Test
    @DisplayName("approveKakaoPay - 카카오페이 API 오류 발생 시 BusinessException 반환")
    void approveKakaoPay_kakaoApiError() {
        // given
        Order order = createValidOrder(ORDER_ID);
        Payment payment = createPayment(order);

        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        when(paymentRepository.findByOrderIdAndTransactionIdIsNotNull(ORDER_ID)).thenReturn(Optional.of(payment));

        when(kakaoPayProps.getCid()).thenReturn("TC0ONETIME");
        when(kakaoPayProps.getApproveUrl()).thenReturn("https://kapi.kakao.com/v1/payment/approve");

        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(HashMap.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);

        // WebClient 오류 응답 시뮬레이션
        when(responseSpec.onStatus(any(), any())).thenAnswer(invocation -> {
            Function<ClientResponse, Mono<? extends Throwable>> errorHandler = invocation.getArgument(1);
            ClientResponse mockResponse = mock(ClientResponse.class);
            when(mockResponse.bodyToMono(String.class)).thenReturn(Mono.just("카카오 오류 응답"));

            // 핵심: 예외 흐름을 강제로 유도
            when(responseSpec.bodyToMono(KakaoPayApproveResponseV1.class))
                    .thenAnswer(inv -> errorHandler.apply(mockResponse).flatMap(Mono::error));

            return responseSpec;
        });

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.approveKakaoPay(ORDER_ID, "pgToken123"));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.KAKAO_API_ERROR);
    }

    @Test
    @DisplayName("카카오페이 응답 객체 자체가 null이면 예외 발생")
    void approveKakaoPay_shouldThrow_whenResponseIsNull() {
        // given
        Order order = createValidOrder(ORDER_ID);
        Payment payment = createPayment(order);

        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        when(paymentRepository.findByOrderIdAndTransactionIdIsNotNull(ORDER_ID)).thenReturn(Optional.of(payment));
        mockApproveKakaoPayProps();
        mockWebClientChain(null, KakaoPayApproveResponseV1.class);

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.approveKakaoPay(ORDER_ID, "pgToken123"));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.KAKAO_API_ERROR);
    }

    @ParameterizedTest(name = "{index}: 카카오페이 응답의 {0} 필드가 null일 때 예외 발생")
    @MethodSource("invalidKakaoPayApproveResponses")
    void approveKakaoPay_shouldThrow_whenFieldIsNull(String desc, KakaoPayApproveResponseV1 response) {
        // given
        Order order = createValidOrder(ORDER_ID);
        Payment payment = createPayment(order);

        when(orderReader.findByIdForUpdate(ORDER_ID)).thenReturn(order);
        when(paymentRepository.findByOrderIdAndTransactionIdIsNotNull(ORDER_ID)).thenReturn(Optional.of(payment));
        mockApproveKakaoPayProps();
        mockWebClientChain(response, KakaoPayApproveResponseV1.class);

        // when & then
        BusinessException exception = assertThrows(BusinessException.class, () -> paymentService.approveKakaoPay(ORDER_ID, "pgToken123"));
        assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.KAKAO_API_ERROR);
    }

    static Stream<Arguments> invalidKakaoPayApproveResponses() {
        return Stream.of(
                Arguments.of("tid", KakaoPayApproveResponseV1.builder()
                        .tid(null)
                        .approvedAt(LocalDateTime.now())
                        .build()),
                Arguments.of("approvedAt", KakaoPayApproveResponseV1.builder()
                        .tid("validTid")
                        .approvedAt(null)
                        .build())
        );
    }

    private Order createValidOrder(Long id) {
        return Order.builder()
                .id(id)
                .userId(user.getId())
                .quantity(1)
                .totalAmount(product.getAmount())
                .product(product)
                .orderStatus(OrderStatus.INITIAL)
                .build();
    }

    private Payment createPayment(Order order) {
        return Payment.builder()
                .order(order)
                .transactionId("TX1234")
                .paymentStatus(PaymentStatus.INITIAL)
                .build();
    }

    private KakaoPayReadyResponseV1 createMockKakaoReadyResponse() {
        return KakaoPayReadyResponseV1.builder()
                .tid("TX1234")
                .nextRedirectPcUrl("https://mock.url")
                .createdAt(LocalDateTime.now())
                .build();
    }

    private KakaoPayApproveResponseV1 createMockKakaoApproveResponse() {
        return KakaoPayApproveResponseV1.builder()
                .aid("0000")
                .tid("TX1234")
                .cid("test01")
                .partnerOrderId("1")
                .partnerUserId(String.valueOf(user.getId()))
                .paymentMethodType("CARD")
                .itemName(product.getProductName())
                .quantity(1)
                .createdAt(LocalDateTime.now())
                .approvedAt(LocalDateTime.now())
                .build();
    }

    private <T> void mockWebClientChain(T response, Class<T> responseType) {
        when(webClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri(anyString())).thenReturn(requestBodySpec);
        when(requestBodySpec.contentType(MediaType.APPLICATION_JSON)).thenReturn(requestBodySpec);
        when(requestBodySpec.bodyValue(any(Map.class))).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.onStatus(any(), any())).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(responseType)).thenReturn(Mono.justOrEmpty(response));
    }

    private void mockPrepareKakaoPayProps() {
        when(kakaoPayProps.getCid()).thenReturn("TC0ONETIME");
        when(kakaoPayProps.getTaxFreeAmount()).thenReturn(0);
        when(kakaoPayProps.getApprovalRedirectUrl(ORDER_ID)).thenReturn("http://localhost/approve");
        when(kakaoPayProps.getCancelRedirectUrl(ORDER_ID)).thenReturn("http://localhost/cancel");
        when(kakaoPayProps.getFailRedirectUrl(ORDER_ID)).thenReturn("http://localhost/fail");
        when(kakaoPayProps.getReadyUrl()).thenReturn("https://kapi.kakao.com/v1/payment/ready");
    }


    private void mockApproveKakaoPayProps() {
        when(kakaoPayProps.getCid()).thenReturn("TC0ONETIME");
        when(kakaoPayProps.getApproveUrl()).thenReturn("https://kapi.kakao.com/v1/payment/approve");
    }
}