package hello.commerce.order.model;

import hello.commerce.common.model.BaseEntity;
import hello.commerce.product.model.Product;
import hello.commerce.user.model.User;
import jakarta.persistence.*;
import lombok.*;


@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Order extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "product_id", nullable = false)
    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column(nullable = false)
    private int totalAmount;

    @Column(nullable = false)
    private int quantity;

    @Column(length = 512)
    private String kakaopayReadyUrl;
}
