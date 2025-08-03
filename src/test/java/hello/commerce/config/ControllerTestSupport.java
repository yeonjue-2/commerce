package hello.commerce.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.commerce.auth.AuthController;
import hello.commerce.common.exception.GlobalExceptionHandler;
import hello.commerce.order.OrderController;
import hello.commerce.order.OrderService;
import hello.commerce.payment.PaymentController;
import hello.commerce.payment.PaymentService;
import hello.commerce.product.ProductController;
import hello.commerce.product.ProductService;
import hello.commerce.user.UserController;
import hello.commerce.user.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

@ActiveProfiles("test")
@WebMvcTest(controllers = {
        ProductController.class,
        OrderController.class,
        PaymentController.class,
        UserController.class,
        AuthController.class
})
@Import({GlobalExceptionHandler.class, TestConfig.class}) // 전역 예외 핸들러 수동 등록
public abstract class ControllerTestSupport {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    protected ProductService productService;

    @MockBean
    protected OrderService orderService;

    @MockBean
    protected PaymentService paymentService;

    @MockBean
    protected UserService userService;

}