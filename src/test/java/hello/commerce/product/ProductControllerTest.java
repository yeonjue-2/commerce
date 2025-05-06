package hello.commerce.product;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Disabled
    @DisplayName("GET /v1/products - return ProductListResponseV1")
    void getProducts_success() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("GET /v1/products - 유효하지 않은 page는 400 반환")
    void getProducts_invalidPage() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("GET /v1/products - 유효하지 않은 size는 400 반환")
    void getProducts_invalidSize() throws Exception {
        throw new UnsupportedOperationException();
    }
}