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
    @DisplayName("GET, return ProductListResponseV1")
    void getProducts_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/products")
                        .param("page", "1")
                        .param("size", "20"))
                .andExpect(status().isOk()); // 200 나오는지만 확인
    }
}