package hello.commerce.product.dto;

import hello.commerce.order.dto.OrderResponseDtoV1;
import hello.commerce.order.model.Order;
import hello.commerce.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDtoV1 {
    private Long id;
    private String name;
    private int amount;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponseDtoV1 fromEntity(Product product) {
        return ProductResponseDtoV1.builder()
                .id(product.getId())
                .name(product.getName())
                .amount(product.getAmount())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
