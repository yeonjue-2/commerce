package hello.commerce.product;

import hello.commerce.common.exception.ErrorCode;
import hello.commerce.config.ControllerTestSupport;
import hello.commerce.product.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ProductControllerTest extends ControllerTestSupport {

    @Test
    @DisplayName("GET /v1/products - return ProductListResponseV1")
    void getProducts_success() throws Exception {

        Product product1 = createProduct(101L);
        Product product2 = createProduct(102L);
        Page<Product> page = new PageImpl<>(List.of(product1, product2), PageRequest.of(0, 20), 1);

        // when
        when(productService.getProducts(any())).thenReturn(page);

        // then
        mockMvc.perform(get("/v1/products")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].productId").value(product1.getId()))
                .andExpect(jsonPath("$.products[1].productId").value(product2.getId()))
                .andExpect(jsonPath("$.totalElements").value(2));
    }

    @Test
    @DisplayName("GET /v1/products - 유효하지 않은 page는 400 반환")
    void getProducts_invalidPage() throws Exception {
        mockMvc.perform(get("/v1/products")
                        .param("page", "0") // 잘못된 값
                        .param("size", "10"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_PAGE.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_PAGE.getMessage()));
    }

    @Test
    @DisplayName("GET /v1/products - 유효하지 않은 size는 400 반환")
    void getProducts_invalidSize() throws Exception {
        mockMvc.perform(get("/v1/products")
                        .param("page", "1")
                        .param("size", "100")) // 잘못된 값
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errorCode").value(ErrorCode.INVALID_SIZE.getCode()))
                .andExpect(jsonPath("$.errorMessage").value(ErrorCode.INVALID_SIZE.getMessage()));
    }

    private Product createProduct(Long id) {
        return Product.builder()
                .id(id)
                .productName("향균 베개 커버")
                .amount(25000)
                .stock(300)
                .build();
    }
}