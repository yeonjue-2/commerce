package hello.commerce.payment;


import hello.commerce.payment.dto.KakaoPayReadyResponseV1;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    @Autowired
    private final PaymentService paymentService;

    /* 주문 -> 결제 api(readyPayment) -> 카카오페이 내부 api 요청
     * 1.결제 승인 2.결제 실패 3.결제 취소
     */
    @PutMapping("/v1/payments/orders/{order_id}/ready")
    public ResponseEntity<KakaoPayReadyResponseV1> readyPayment(@PathVariable("order_id") Long orderId) {
        KakaoPayReadyResponseV1 kakaoPayReadyResponseV1 = paymentService.prepareKakaoPay(orderId);
        return ResponseEntity.ok(kakaoPayReadyResponseV1);
    }

    // 1. 결제 승인
    @GetMapping("/v1/payments/orders/{orderId}/approve")
    public ResponseEntity<String> approvePayment(@PathVariable Long orderId,
                                                 @RequestParam("pg_token") String pgToken) {
        paymentService.approveKakaoPay(orderId, pgToken);
        return ResponseEntity.ok("결제 완료되었습니다.");
    }

    // 2. 결제 실패
    @GetMapping("/v1/payments/orders/{orderId}/fail")
    public ResponseEntity<String> fail(@PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제에 실패했습니다.");
    }

    // 3. 결제 취소
    @GetMapping("/v1/payments/orders/{orderId}/cancel")
    public ResponseEntity<String> cancel(@PathVariable Long orderId) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("결제가 취소되었습니다.");
    }
}
