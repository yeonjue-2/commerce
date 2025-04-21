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
public class OrderDto {
    private Long id;
    private Long userId;
    private Long productId;
    private OrderStatus orderStatus;
    private int totalAmount;
    private int quantity;
    private String kakaopayReadyUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static OrderDto fromEntity(Order order) {
        return OrderDto.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .productId(order.getProductId())
                .orderStatus(order.getOrderStatus())
                .totalAmount(order.getTotalAmount())
                .quantity(order.getQuantity())
                .kakaopayReadyUrl(order.getKakaopayReadyUrl())
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }
}
