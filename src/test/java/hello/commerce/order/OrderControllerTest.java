package hello.commerce.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.commerce.common.error.GlobalExceptionHandler;
import hello.commerce.common.model.BusinessException;
import hello.commerce.common.model.ErrorCode;
import hello.commerce.config.TestConfig;
import hello.commerce.order.dto.OrderRequestV1;
import hello.commerce.order.dto.OrderResponseV1;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import hello.commerce.product.model.Product;
import hello.commerce.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(OrderController.class)
@Import({GlobalExceptionHandler.class, TestConfig.class}) // 전역 예외 핸들러 수동 등록
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    OrderService orderService;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    User user;
    Product product;

    @BeforeEach
    void setUp() {
        user = new User(100L, "nonamed");
        product = new Product(100L, "향균 베개 커버", 35000, 200);
    }


    @Test
    @DisplayName("POST /v1/orders - 성공 시 201 Created")
    void createOrder_success() throws Exception {
        // given
        OrderRequestV1 request = new OrderRequestV1(user.getId(), product.getId(), 10);
        OrderResponseV1 expectedResponse = OrderResponseV1.fromEntity(createOrder(100L, product.getAmount(), 10));

        // when
        when(orderService.createOrder(any())).thenReturn(createOrder(product.getId(), product.getAmount(), 10));

        // then
        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/v1/orders/100"))
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResponse)));
    }

    @Test
    @DisplayName("POST /v1/orders - 유효하지 않은 주문 수량 400 Bad Request")
    void createOrder_invalidOrderQuantity() throws Exception {
        // given
        OrderRequestV1 request = new OrderRequestV1(user.getId(), product.getId(), 0);  // 수량 오류, @Valid에서 걸림

        // then
        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_ORDER_QUANTITY.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_ORDER_QUANTITY.getMessage()));

    }

    @Test
    @DisplayName("POST /v1/orders - 존재하지 않는 상품 404 Not Found")
    void createOrder_notFoundProduct() throws Exception {
        // given
        OrderRequestV1 request = new OrderRequestV1(user.getId(), product.getId(), 10);

        // when
        when(orderService.createOrder(any())).thenThrow(new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        // then
        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND_PRODUCT.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.NOT_FOUND_PRODUCT.getMessage()));
    }

    @Test
    @DisplayName("POST /v1/orders - 재고 부족 409 Conflict")
    void createOrder_insufficientStock() throws Exception {
        // given
        OrderRequestV1 request = new OrderRequestV1(user.getId(), product.getId(), 10);

        // when
        when(orderService.createOrder(any())).thenThrow(new BusinessException(ErrorCode.INSUFFICIENT_STOCK));

        // then
        mockMvc.perform(post("/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INSUFFICIENT_STOCK.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INSUFFICIENT_STOCK.getMessage()));
    }

    @Test
    @DisplayName("GET /v1/orders - 정상 요청 시 200 OK (orderStatus 파라미터 O)")
    void getOrders_successWithOrderStatus() throws Exception {
        // given
        Order order1 = createOrder(product.getId(), product.getAmount(), product.getStock());
        Page<Order> page = new PageImpl<>(List.of(order1), PageRequest.of(0, 20), 1);

        when(orderService.getOrders(any(), eq(OrderStatus.PAID))).thenReturn(page);

        // when & then
        mockMvc.perform(get("/v1/orders")
                        .param("page", "1")
                        .param("size", "20")
                        .param("order_status", "PAID"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].orderId").value(product.getId()))
                .andExpect(jsonPath("$.orders[0].quantity").value(product.getStock()))
                .andExpect(jsonPath("$.totalElements").value(1));
    }

    @Test
    @DisplayName("GET /v1/orders - 정상 요청 시 200 OK (orderStatus 파라미터 X)")
    void getOrders_successWithNoParameter() throws Exception {
        // given
        Order order1 = createOrder(product.getId(), product.getAmount(), product.getStock());
        Page<Order> page = new PageImpl<>(List.of(order1), PageRequest.of(0, 20), 1);

        when(orderService.getOrders(any())).thenReturn(page);

        // when & then
        mockMvc.perform(get("/v1/orders")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders[0].orderId").value(product.getId()))
                .andExpect(jsonPath("$.orders[0].quantity").value(product.getStock()))
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
    @DisplayName("GET /v1/orders/{order_id} - 정상 요청 시 200 OK")
    void getOrderById_success() throws Exception {
        // given
        Order order = createOrder(product.getId(), product.getAmount(), product.getStock());
        when(orderService.getOrderById(any())).thenReturn(order);

        // when & then
        mockMvc.perform(get("/v1/orders/{order_id}", order.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orderId").value(order.getId()))
                .andExpect(jsonPath("$.orderStatus").value(order.getOrderStatus().name()));
    }

    @Test
    @DisplayName("GET /v1/orders/{order_id} - 파라미터 타입이 잘못되면 400 반환")
    void getOrderById_invalidOrderIdParam() throws Exception {
        mockMvc.perform(get("/v1/orders/{order_id}", "abc"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_ORDER_ID_PARAM.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_ORDER_ID_PARAM.getMessage()));
    }

    @Test
    @DisplayName("GET /v1/orders/{order_id} - order 데이터를 찾을 수 없으면 404 반환")
    void getOrderById_notFoundOrder() throws Exception {
        // given
        when(orderService.getOrderById(999L))
                .thenThrow(new BusinessException(ErrorCode.NOT_FOUND_ORDER));

        // when & then
        mockMvc.perform(get("/v1/orders/{order_id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.NOT_FOUND_ORDER.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.NOT_FOUND_ORDER.getMessage()));
    }

    private Order createOrder(Long id, int totalAmount, int quantity) {
        Order order = Order.builder()
                .id(id)
                .userId(user.getId())
                .product(product)
                .orderStatus(OrderStatus.INITIAL)
                .totalAmount(totalAmount)
                .quantity(quantity)
                .kakaopayReadyUrl("https://kakao.url/ready")
                .build();
        return order;
    }
}