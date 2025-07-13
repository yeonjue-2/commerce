package hello.commerce.order.dto;

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
    private String productName;
    private OrderStatus orderStatus;
    private int totalAmount;
    private int quantity;
    private String kakaoPayReadyUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
