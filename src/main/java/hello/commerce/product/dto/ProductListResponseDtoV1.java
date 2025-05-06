package hello.commerce.product.dto;

import hello.commerce.product.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponseDtoV1 {
    private List<ProductResponseV1> products;
    private int currentPage;
    private int totalPages;
    private long totalElements;

    public static ProductListResponseDtoV1 fromEntities(Page<Product> productPage) {

        List<ProductResponseV1> productDtos = productPage.getContent().stream()
                .map(ProductResponseV1::fromEntity)
                .collect(Collectors.toList());

        return ProductListResponseDtoV1.builder()
                .products(productDtos)
                .currentPage(productPage.getNumber() + 1)
                .totalPages(productPage.getTotalPages())
                .totalElements(productPage.getTotalElements())
                .build();
    }
}
