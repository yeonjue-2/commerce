package hello.commerce.product.dto;

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
public class ProductResponseV1 {
    private Long productId;
    private String productName;
    private int productAmount;
    private int stock;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ProductResponseV1 fromEntity(Product product) {
        return ProductResponseV1.builder()
                .productId(product.getId())
                .productName(product.getProductName())
                .productAmount(product.getAmount())
                .stock(product.getStock())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
