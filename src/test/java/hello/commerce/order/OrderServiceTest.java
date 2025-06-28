package hello.commerce.order;

import hello.commerce.common.exception.BusinessException;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.order.dto.OrderRequestV1;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import hello.commerce.product.ProductReader;
import hello.commerce.product.model.Product;
import hello.commerce.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

    @Mock
    ProductReader productReader;

    @InjectMocks
    OrderServiceImpl orderService;

    User user;
    Product product;

    @BeforeEach
    void setUp() {
        user = new User(100L, "nonamed");
        product = new Product(100L, "향균 베개 커버", 35000, 200);
    }

    @Test
    @DisplayName("createOrder 성공")
    void createOrder_success() {
        // given
        int quantity = 10;
        int expeditedAmount = product.getAmount() * quantity;

        OrderRequestV1 request = createOrderRequestWithQuantity(quantity);

        Order savedOrder = createOrder(1L, expeditedAmount, quantity);
        when(productReader.findByIdForUpdate(product.getId())).thenReturn(product);
        mockOrderSaveWithId(1L);
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(savedOrder));

        // when
        Order order = orderService.createOrder(request);

        // then
        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(savedOrder.getId());
        assertThat(order.getQuantity()).isEqualTo(quantity);
        assertThat(order.getTotalAmount()).isEqualTo(expeditedAmount);
        assertThat(product.getStock()).isEqualTo(200 - quantity);
    }

    @Test
    @DisplayName("createOrder 실패, 요청 수량이 0보다 작음 - INVALID_ORDER_QUANTITY")
    void createOrder_failForQuantity() {
        // given
        OrderRequestV1 request = createOrderRequestWithQuantity(0);

        // when & then
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(request);
        });

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INVALID_ORDER_QUANTITY);
    }

    @Test
    @DisplayName("createOrder 실패, 상품을 찾을 수 없을 때 - NOT_FOUND_PRODUCT")
    void createOrder_failForNotFoundProduct() {
        // given
        OrderRequestV1 request = createOrderRequestWithQuantity(2);
        when(productReader.findByIdForUpdate(product.getId())).thenThrow(new BusinessException(ErrorCode.NOT_FOUND_PRODUCT));

        // when & then
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(request);
        });

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_PRODUCT);
    }

    @Test
    @DisplayName("createOrder 실패, 요청 수량이 재고보다 많을 때 - INSUFFICIENT_STOCK")
    void createOrder_failForInsufficientStock() {
        // given
        int quantity = product.getStock() + 200;  // 현재 상품 재고 + 200

        OrderRequestV1 request = createOrderRequestWithQuantity(quantity);

        when(productReader.findByIdForUpdate(product.getId())).thenReturn(product);

        // when & then
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(request);
        });

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.INSUFFICIENT_STOCK);
    }

    @Test
    @DisplayName("createOrder 실패, 응답 반환 시 주문 데이터가 없을 경우 - INSUFFICIENT_STOCK")
    void createOrder_notFoundOrder() {
        // given
        int quantity = 10;
        int expeditedAmount = product.getAmount() * quantity;

        OrderRequestV1 request = createOrderRequestWithQuantity(quantity);
        Order savedOrder = createOrder(1L, expeditedAmount, quantity);

        when(productReader.findByIdForUpdate(product.getId())).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenReturn(savedOrder);
        when(orderRepository.findById(nullable(Long.class))).thenReturn(Optional.empty());

        // when & then
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            orderService.createOrder(request);
        });

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_ORDER);
    }

    @Test
    @DisplayName("getOrderById 성공, OrderStatus 파라미터 포함)")
    void getOrders_successWithOrderStatus() {
        // given
        OrderStatus filter = OrderStatus.INITIAL;
        PageRequest pageable = PageRequest.of(0, 10);
        List<Order> orders = List.of(createOrder(100L, 35000, 1), createOrder(101L, 35000, 1));
        Page<Order> page = new PageImpl<>(orders);

        when(orderRepository.findAllByOrderStatus(pageable, filter)).thenReturn(page);

        // when
        Page<Order> result = orderService.getOrders(pageable, filter);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(100L);
        assertThat(result.getContent().get(1).getOrderStatus()).isEqualTo(OrderStatus.INITIAL);
    }

    @Test
    @DisplayName("getOrderById 성공, OrderStatus 파라미터 포함 X)")
    void getOrders_successWithNoOrderStatus() {
        // given
        PageRequest pageable = PageRequest.of(0, 10);
        List<Order> orders = List.of(createOrder(100L, 35000, 1), createOrder(101L, 35000, 1));
        Page<Order> page = new PageImpl<>(orders);

        when(orderRepository.findAll(pageable)).thenReturn(page);

        // when
        Page<Order> result = orderService.getOrders(pageable);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(1).getId()).isEqualTo(101L);
    }

    @Test
    @DisplayName("orderId로 주문 조회 성공")
    void getOrdersByOrderId_success() {
        // given
        Order order = createOrder(101L, 35000, 1);
        when(orderRepository.findById(any())).thenReturn(Optional.of(order));

        // when
        Order result = orderService.getOrderById(101L);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(order.getId());
        assertThat(result.getProduct().getId()).isEqualTo(order.getProduct().getId());
    }

    @Test
    @DisplayName("존재하지 않는 주문일 경우 ErrorCode.NOT_FOUND_ORDER 발생")
    void getOrdersByOrderId_notFoundOrder() {
        // given
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        // when & then
        BusinessException ex = assertThrows(BusinessException.class, () -> {
            orderService.getOrderById(999L);
        });

        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_ORDER);
    }


    private Order createOrder(Long id, int totalAmount, int quantity) {
        return Order.builder()
                .id(id)
                .userId(user.getId())
                .product(product)
                .orderStatus(OrderStatus.INITIAL)
                .totalAmount(totalAmount)
                .quantity(quantity)
                .build();
    }

    private OrderRequestV1 createOrderRequestWithQuantity(int quantity) {
        return OrderRequestV1.builder()
                .userId(user.getId())
                .productId(product.getId())
                .quantity(quantity)
                .build();
    }

    private void mockOrderSaveWithId(long id) {
        when(orderRepository.save(any(Order.class)))
                .thenAnswer(invocation -> {
                    Order saved = invocation.getArgument(0);
                    saved.setId(id); // 실제 객체에 ID 지정
                    return saved;
                });
    }
}
