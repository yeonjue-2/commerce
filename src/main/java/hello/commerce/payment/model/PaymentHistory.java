package hello.commerce.payment.model;

import hello.commerce.common.model.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payment_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentHistory extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @Column(name = "order_id", nullable = false)
    private Long orderId;

    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false)
    private PaymentStatus paymentStatus;

    @Column(name = "total_amount", nullable = false)
    private int totalAmount;

    private String pgToken;

    private String transactionId;

    private LocalDateTime paidAt;

    private LocalDateTime canceledAt;

    private String failReason;

    @Column(name = "is_test", nullable = false)
    private boolean isTest;

    public PaymentHistory(Payment payment) {
        this.payment = payment;
        this.orderId = payment.getOrder().getId();
        this.paymentMethod = payment.getPaymentMethod();
        this.paymentStatus = payment.getPaymentStatus();
        this.totalAmount = payment.getTotalAmount();
        this.pgToken = payment.getPgToken();
        this.transactionId = payment.getTransactionId();
        this.paidAt = payment.getPaidAt();
        this.canceledAt = payment.getCanceledAt();
        this.failReason = payment.getFailReason();
        this.isTest = payment.isTest();
    }
}

