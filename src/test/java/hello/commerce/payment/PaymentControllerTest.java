package hello.commerce.payment;

import hello.commerce.common.error.GlobalExceptionHandler;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(PaymentController.class)
@Import({GlobalExceptionHandler.class})
class PaymentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PaymentService paymentService;

    @Test
    @Disabled
    @DisplayName("PUT /v1/payments/orders/{order_id}/ready - 결제 준비 성공 200 OK")
    void prepareKakaoPay_success() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("PUT /v1/payments/orders/{order_id}/ready - 주문 id의 파라미터 타입이 잘못되면 400 반환")
    void prepareKakaoPay_invalidOrderIdParam() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("PUT /v1/payments/orders/{order_id}/ready - 주문 상태가 INITIAL이 아니면 400 반환")
    void prepareKakaoPay_invalidOrderStatusTransition() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("PUT /v1/payments/orders/{order_id}/ready - order 데이터를 찾을 수 없으면 404 반환")
    void prepareKakaoPay_notFoundOrder() throws Exception {
        throw new UnsupportedOperationException();
    }
}