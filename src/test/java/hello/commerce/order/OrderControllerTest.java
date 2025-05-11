package hello.commerce.order;

import hello.commerce.common.error.GlobalExceptionHandler;
import hello.commerce.common.model.ErrorCode;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import hello.commerce.product.model.Product;
import hello.commerce.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(OrderController.class)
@Import(GlobalExceptionHandler.class) // 전역 예외 핸들러 수동 등록
class OrderControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    User user;
    Product product;

    @BeforeEach
    void setUp() {
        user = new User(100L, "nonamed");
        product = new Product(100L, "향균 베개 커버", 35000, 200);
    }

    @Test
    @DisplayName("GET /v1/orders - 정상 요청 시 200 OK (orderStatus 파라미터 O)")
    void getOrders_successWithOrderStatus() throws Exception {
        // given
        Order order1 = createOrder(100L);
        Page<Order> page = new PageImpl<>(List.of(order1), PageRequest.of(0, 20), 1);

        when(orderService.getOrders(any(), eq(OrderStatus.PAID))).thenReturn(page);

        // when & then
        mockMvc.perform(get("/v1/orders")
                        .param("page", "1")
                        .param("size", "20")
                        .param("order_status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].orderId").value(100))
                .andExpect(jsonPath("$.orders[0].quantity").value(2))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("GET /v1/orders - 정상 요청 시 200 OK (orderStatus 파라미터 X)")
    void getOrders_successWithNoParameter() throws Exception {
        // given
        Order order1 = createOrder(100L);
        Page<Order> page = new PageImpl<>(List.of(order1), PageRequest.of(0, 20), 1);

        when(orderService.getOrders(any())).thenReturn(page);

        // when & then
        mockMvc.perform(get("/v1/orders")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].orderId").value(100))
                .andExpect(jsonPath("$.orders[0].quantity").value(2))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("GET /v1/orders - 유효하지 않은 page는 400 반환")
    void getOrders_invalidPage() throws Exception {
        mockMvc.perform(get("/v1/orders")
                        .param("page", "0") // 잘못된 값
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_PAGE.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_PAGE.getMessage()));
    }

    @Test
    @DisplayName("GET /v1/orders - 유효하지 않은 size는 400 반환")
    void getOrders_invalidSize() throws Exception {
        mockMvc.perform(get("/v1/orders")
                        .param("page", "1")
                        .param("size", "1000")) // 잘못된 값
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_SIZE.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_SIZE.getMessage()));
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

    private Order createOrder(Long id) {
        Order order = Order.builder()
                .id(id)
                .user(user)
                .product(product)
                .orderStatus(OrderStatus.PAID)
                .quantity(2)
                .totalAmount(70000)
                .kakaopayReadyUrl("https://kakao.url/ready")
                .build();
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return order;
    }
}