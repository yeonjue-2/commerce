package hello.commerce.order;

import hello.commerce.common.model.BusinessException;
import hello.commerce.common.model.ErrorCode;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import hello.commerce.product.model.Product;
import hello.commerce.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    OrderRepository orderRepository;

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
    @DisplayName("getOrderById 성공, OrderStatus 파라미터 포함)")
    void getOrders_successWithOrderStatus() {
        // given
        OrderStatus filter = OrderStatus.PAID;
        PageRequest pageable = PageRequest.of(0, 10);
        List<Order> orders = List.of(createOrder(100L), createOrder(101L));
        Page<Order> page = new PageImpl<>(orders);

        when(orderRepository.findAllByOrderStatus(pageable, filter)).thenReturn(page);

        // when
        Page<Order> result = orderService.getOrders(pageable, filter);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(0).getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("getOrderById 성공, OrderStatus 파라미터 포함 X)")
    void getOrders_successWithNoOrderStatus() {
        // given
        PageRequest pageable = PageRequest.of(0, 10);
        List<Order> orders = List.of(createOrder(100L), createOrder(101L));
        Page<Order> page = new PageImpl<>(orders);

        when(orderRepository.findAll(pageable)).thenReturn(page);

        // when
        Page<Order> result = orderService.getOrders(pageable, null);

        // then
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent().get(1).getId()).isEqualTo(101L);
    }

    @Test
    @DisplayName(("orderId로 주문 조회 성공"))
    void getOrdersByOrderId_success() {
        // given
        Order order = createOrder(101L);
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


    private Order createOrder(Long id) {
        return Order.builder()
                .id(id)
                .user(user)
                .product(product)
                .orderStatus(OrderStatus.PAID)
                .totalAmount(35000)
                .quantity(1)
                .build();
    }
}
