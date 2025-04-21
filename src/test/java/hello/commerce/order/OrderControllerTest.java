package hello.commerce.order;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(OrderController.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @Disabled
    @DisplayName("GET, return OrderListResponseV1")
    void getOrders_success() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/v1/orders")
                .param("page", "1")
                .param("size", "20")
                .param("order_status", "INITIAL"))
                .andExpect(status().isOk()); // 200 나오는지만 확인
    }
}