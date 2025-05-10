package hello.commerce.order;

import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import hello.commerce.product.model.Product;
import hello.commerce.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(result.getContent().get(1).getId()).isEqualTo(10L);
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
