package hello.commerce.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.commerce.common.exception.ErrorCode;
import hello.commerce.product.model.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    ProductService productService;

    @Autowired
    ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("GET /v1/products - return ProductListResponseV1")
    void getProducts_success() throws Exception {

        Product product = createProduct();
        Page<Product> page = new PageImpl<>(List.of(product), PageRequest.of(0, 20), 1);

        // when
        when(productService.getProducts(any())).thenReturn(page);

        // then
        mockMvc.perform(get("/v1/products")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.products[0].productId").value(product.getId()))
                .andExpect(jsonPath("$.products[0].productName").value(product.getName()))
                .andExpect(jsonPath("$.totalElements").value(1));
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

    private Product createProduct() throws Exception {
        return Product.builder()
                .id(100L)
                .name("향균 베개 커버")
                .amount(25000)
                .stock(300)
                .build();
    }
}