package hello.commerce.payment.model;

import hello.commerce.common.model.BaseEntity;
import hello.commerce.order.model.Order;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod;

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    @Column(name = "pg_token")
    private String pgToken;

    @Column(name = "transaction_id", nullable = false, length = 100)
    private String transactionId;

    private LocalDateTime paidAt;

    private LocalDateTime canceledAt;

    private String failReason;

    @Column(name = "is_test", nullable = false)
    private boolean isTest;

}
