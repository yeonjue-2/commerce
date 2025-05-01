package hello.commerce.order;

import jdk.jshell.spi.ExecutionControl;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Disabled
    @DisplayName("GET /v1/orders - return OrderListResponseV1")
    void getOrders_success() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("GET /v1/orders - 유효하지 않은 page는 400 반환")
    void getOrders_invalidPage() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("GET /v1/orders - 유효하지 않은 size는 400 반환")
    void getOrders_invalidSize() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("GET /v1/orders - 유효하지 않은 order_status는 400 반환")
    void getOrders_invalidOrderStatus() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("GET /v1/orders/{order_id} - return OrderDto")
    void getOrderById_success() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("GET /v1/orders/{order_id} - 유효하지 않은 order_id는 400 반환")
    void getOrderById_invalidOrderIdParam() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("GET /v1/orders/{order_id} - order 데이터를 찾을 수 없으면 404 반환")
    void getOrderById_notFoundOrder() throws Exception {
        throw new UnsupportedOperationException();
    }
}