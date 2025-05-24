package hello.commerce.order;

import hello.commerce.order.dto.OrderResponseV1;
import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import hello.commerce.product.model.Product;
import hello.commerce.user.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class OrderResponseV1Test {

    User user;
    Product product;

    @BeforeEach
    void setUp() {
        user = new User(100L, "nonamed");
        product = new Product(100L, "향균 베개 커버", 35000, 200);
    }

    @Test
    void fromEntity_shouldConvertOrderToDtoCorrectly() {
        // given
        Order order = createOrder(100L);

        // when
        OrderResponseV1 dto = OrderResponseV1.fromEntity(order);

        // then
        assertThat(dto.getOrderId()).isEqualTo(100L);
        assertThat(dto.getUserId()).isEqualTo(100L);
        assertThat(dto.getProductId()).isEqualTo(100L);
        assertThat(dto.getOrderStatus()).isEqualTo(OrderStatus.PAID);
        assertThat(dto.getQuantity()).isEqualTo(2);
        assertThat(dto.getTotalAmount()).isEqualTo(70000);
        assertThat(dto.getKakaopayReadyUrl()).isEqualTo("https://kakaopay.url/ready");
    }

    private Order createOrder(Long id) {
        Order order = Order.builder()
                .id(id)
                .userId(user.getId())
                .product(product)
                .orderStatus(OrderStatus.PAID)
                .quantity(2)
                .totalAmount(70000)
                .kakaopayReadyUrl("https://kakaopay.url/ready")
                .build();
        order.setCreatedAt(LocalDateTime.now());
        order.setUpdatedAt(LocalDateTime.now());
        return order;
    }
}
