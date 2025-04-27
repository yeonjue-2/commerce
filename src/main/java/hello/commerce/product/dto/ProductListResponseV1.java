package hello.commerce.product.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductListResponseV1 {
    private List<ProductListResponseV1> orders;
    private int currentPage;
    private int totalPages;
    private long totalElements;
}
