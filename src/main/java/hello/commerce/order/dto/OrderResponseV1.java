package hello.commerce.order.dto;

import hello.commerce.order.model.Order;
import hello.commerce.order.model.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponseV1 {
    private Long orderId;

    private Long userId;
    private Long productId;
    private OrderStatus orderStatus;
    private int totalAmount;
    private int quantity;
    private String kakaopayReadyUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderResponseV1 fromEntity(Order order) {
        return OrderResponseV1.builder()
                .orderId(order.getId())
                .userId(order.getUser().getId())
                .productId(order.getProduct().getId())
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .quantity(order.getQuantity())
                .kakaopayReadyUrl(order.getKakaopayReadyUrl())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
