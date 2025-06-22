package hello.commerce.payment;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.common.response.ApiResponse;
import hello.commerce.config.ControllerTestSupport;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import hello.commerce.payment.dto.KakaoPayReadyResponseV1;
import hello.commerce.product.model.Product;
import hello.commerce.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static hello.commerce.payment.PaymentController.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PaymentControllerTest extends ControllerTestSupport {

    Order order;
    User user;
    Product product;

    @BeforeEach
    void setUp() {
        user = new User(100L, "nonamed");
        product = new Product(100L, "향균 베개 커버", 35000, 200);
        order = Order.builder()
                .id(100L)
                .userId(user.getId())
                .product(product)
                .orderStatus(OrderStatus.INITIAL)
                .totalAmount(105000)
                .quantity(3)
                .kakaopayReadyUrl("https://kakao.url/ready")
                .build();
    }

    @Test
    @DisplayName("PUT /v1/payments/orders/{order_id}/ready - 결제 준비 성공 및 결제 정보 저장 200 OK")
    void readyPayment_success() throws Exception {
        // given
        KakaoPayReadyResponseV1 expectedResponse = KakaoPayReadyResponseV1.builder()
                .tid("T123456789")
                .nextRedirectPcUrl("https://mock.kakao.com/redirect")
                .createdAt(LocalDateTime.now())
                .build();

        // when
        when(paymentService.prepareKakaoPay(any())).thenReturn(expectedResponse);

        mockMvc.perform(put("/v1/payments/orders/{order_id}/ready", order.getId()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));

    }

    @Test
    @DisplayName("PUT /v1/payments/orders/{order_id}/ready - 주문 id의 파라미터 타입이 잘못되면 400 반환")
    void readyPayment_invalidOrderIdParam() throws Exception {
        // given
        String invalidOrderId = "invalidOrderId";

        // then
        mockMvc.perform(put("/v1/payments/orders/{order_id}/ready", invalidOrderId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_ORDER_ID_PARAM.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_ORDER_ID_PARAM.getMessage()));

    }

    @Test
    @DisplayName("PUT /v1/payments/orders/{order_id}/ready - 주문 상태가 INITIAL이 아니면 400 반환")
    void readyPayment_invalidOrderStatusTransition() throws Exception {
        // when
        when(paymentService.prepareKakaoPay(any())).thenThrow(new BusinessException(ErrorCode.INVALID_ORDER_STATUS_TRANSITION));

        // then
        mockMvc.perform(put("/v1/payments/orders/{order_id}/ready", 1L))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_ORDER_STATUS_TRANSITION.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_ORDER_STATUS_TRANSITION.getMessage()));
    }

    @Test
    @DisplayName("PUT /v1/payments/orders/{order_id}/ready - order 데이터를 찾을 수 없으면 404 반환")
    void readyPayment_notFoundOrder() throws Exception {
        // when
        when(paymentService.prepareKakaoPay(any())).thenThrow(new BusinessException(ErrorCode.NOT_FOUND_ORDER));

        // then
        mockMvc.perform(put("/v1/payments/orders/{order_id}/ready", 1L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errorCode").value(ErrorCode.NOT_FOUND_ORDER.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.NOT_FOUND_ORDER.getMessage()));
    }

    @Test
    @DisplayName("GET /v1/payments/orders/{order_id}/approve 결제 승인 성공")
    void approvePayment_success() throws Exception {
        // given
        String pgToken = "sample_pgToken";

        // expected JSON
        String expectedJson = objectMapper.writeValueAsString(
                new ApiResponse<>(HttpStatus.OK, PAYMENT_APPROVE_SUCCESS_MESSAGE, null)
        );

        // when & then
        mockMvc.perform(get("/v1/payments/orders/{orderId}/approve", order.getId())
                        .param("pg_token", pgToken))
                .andExpect(status().isOk())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @DisplayName("GET /v1/payments/orders/{order_id}/approve - 주문 id의 파라미터 타입 오류 (400 반환)")
    void approvePayment_invalidOrderIdParam() throws Exception {
        // given
        String invalidOrderId = "invalidOrderId";

        // then
        mockMvc.perform(get("/v1/payments/orders/{order_id}/approve", invalidOrderId))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_ORDER_ID_PARAM.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_ORDER_ID_PARAM.getMessage()));
    }

    @Test
    @DisplayName("GET /v1/payments/orders/{order_id}/approve - pgToken 누락 시 (400 반환)")
    void approvePayment_missingPgToken() throws Exception {
        // then
        mockMvc.perform(get("/v1/payments/orders/{order_id}/approve", order.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_PG_TOKEN_PARAM.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_PG_TOKEN_PARAM.getMessage()));
    }

    @Test
    @DisplayName("GET /v1/payments/orders/{order_id}/approve - 서비스 호출 중 유효성 검사 (주문 상태가 INITIAL이 아님)")
    void approvePayment_invalidOrderStatusTransition() throws Exception {
        String pgToken = "sample_pgToken";

        doThrow(new BusinessException(ErrorCode.INVALID_ORDER_STATUS_TRANSITION))
                .when(paymentService).approveKakaoPay(order.getId(), pgToken);

        mockMvc.perform(get("/v1/payments/orders/{order_id}/approve", order.getId())
                        .param("pg_token", pgToken))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("errorCode").value(ErrorCode.INVALID_ORDER_STATUS_TRANSITION.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_ORDER_STATUS_TRANSITION.getMessage()));
    }

    @Test
    @DisplayName("GET /v1/payments/orders/{order_id}/approve - 서비스 호출 중 유효성 검사 (주문 data 없음)")
    void approvePayment_notFoundOrder() throws Exception {
        String pgToken = "sample_pgToken";

        doThrow(new BusinessException(ErrorCode.NOT_FOUND_ORDER))
                .when(paymentService).approveKakaoPay(order.getId(), pgToken);

        mockMvc.perform(get("/v1/payments/orders/{order_id}/approve", order.getId())
                        .param("pg_token", pgToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errorCode").value(ErrorCode.NOT_FOUND_ORDER.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.NOT_FOUND_ORDER.getMessage()));
    }

    @Test
    @DisplayName("GET /v1/payments/orders/{order_id}/approve - 서비스 호출 중 결제 data 없음")
    void approvePayment_notFoundPayment() throws Exception {
        String pgToken = "sample_pgToken";

        doThrow(new BusinessException(ErrorCode.NOT_FOUND_PAYMENT))
                .when(paymentService).approveKakaoPay(order.getId(), pgToken);

        mockMvc.perform(get("/v1/payments/orders/{order_id}/approve", order.getId())
                        .param("pg_token", pgToken))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("errorCode").value(ErrorCode.NOT_FOUND_PAYMENT.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.NOT_FOUND_PAYMENT.getMessage()));
    }

    @Test
    @DisplayName("GET /v1/payments/orders/{order_id}/fail 결제 승인 요청 실패")
    void handlePaymentFailure() throws Exception {
        // expected JSON
        String expectedJson = objectMapper.writeValueAsString(
                new ApiResponse<>(HttpStatus.BAD_REQUEST, PAYMENT_APPROVE_FAIL_MESSAGE, null)
        );

        // when & then
        mockMvc.perform(get("/v1/payments/orders/{order_id}/fail", order.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson));
    }

    @Test
    @DisplayName("GET /v1/payments/orders/{order_id}/cancel 결제 승인 요청 취소")
    void handlePaymentCancel() throws Exception {
        // expected JSON
        String expectedJson = objectMapper.writeValueAsString(
                new ApiResponse<>(HttpStatus.BAD_REQUEST, PAYMENT_APPROVE_CANCEL_MESSAGE, null)
        );
        // when & then
        mockMvc.perform(get("/v1/payments/orders/{order_id}/cancel", order.getId()))
                .andExpect(status().isBadRequest())
                .andExpect(content().json(expectedJson));
    }
}