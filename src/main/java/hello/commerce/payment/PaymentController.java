package hello.commerce.payment;


import hello.commerce.payment.dto.KakaoPayReadyResponseV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    @PutMapping("/v1/payments/orders/{order_id}/ready")
    public ResponseEntity<KakaoPayReadyResponseV1> readyPayment(@PathVariable("order_id") String orderId) {
        throw new UnsupportedOperationException();
    }
}
