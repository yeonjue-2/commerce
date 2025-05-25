package hello.commerce.payment;

import hello.commerce.order.OrderRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.reactive.function.client.WebClient;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private WebClient kakaoWebClient;

    @Test
    @Disabled
    @DisplayName("prepareKakaoPay - 성공적으로 결제 준비 요청")
    void prepareKakaoPay_success() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("prepareKakaoPay - 존재하지 않는 주문이면 NOT_FOUND_ORDER 예외 발생")
    void prepareKakaoPay_notFoundOrder() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("prepareKakaoPay - 주문 상태가 INITIAL이 아니면 INVALID_ORDER_STATUS_TRANSITION 발생")
    void prepareKakaoPay_invalidOrderStatus() {
        throw new UnsupportedOperationException();
    }

}