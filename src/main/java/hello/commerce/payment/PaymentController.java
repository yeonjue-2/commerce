package hello.commerce.payment;


import hello.commerce.common.response.ApiResponse;
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

    public static final String PAYMENT_APPROVE_SUCCESS_MESSAGE = "결제 완료되었습니다.";
    public static final String PAYMENT_APPROVE_FAIL_MESSAGE = "결제에 실패했습니다.";
    public static final String PAYMENT_APPROVE_CANCEL_MESSAGE = "결제가 취소되었습니다.";

    /* 주문 -> 결제 api(readyPayment) -> 카카오페이 내부 api 요청
     * 1.결제 승인 2.결제 실패 3.결제 취소
     */
    @PutMapping("/v1/payments/orders/{order_id}/ready")
    public ResponseEntity<KakaoPayReadyResponseV1> readyPayment(@PathVariable("order_id") Long orderId) {
        KakaoPayReadyResponseV1 kakaoPayReadyResponseV1 = paymentService.prepareKakaoPay(orderId);
        return ResponseEntity.ok(kakaoPayReadyResponseV1);
    }

    // 1. 결제 승인
    @GetMapping("/v1/payments/orders/{order_id}/approve")
    public ResponseEntity<ApiResponse<Void>> approvePayment(@PathVariable("order_id") Long orderId,
                                                              @RequestParam("pg_token") String pgToken) {
        paymentService.approveKakaoPay(orderId, pgToken);
        return ResponseEntity.ok(new ApiResponse<>(HttpStatus.OK, PAYMENT_APPROVE_SUCCESS_MESSAGE, null));
    }

    // 2. 결제 실패
    @GetMapping("/v1/payments/orders/{order_id}/fail")
    public ResponseEntity<ApiResponse<Void>> handlePaymentFailure(@PathVariable("order_id") Long orderId) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ApiResponse<>(HttpStatus.BAD_REQUEST, PAYMENT_APPROVE_FAIL_MESSAGE, null));
    }

    // 3. 결제 취소
    @GetMapping("/v1/payments/orders/{order_id}/cancel")
    public ResponseEntity<ApiResponse<Void>> handlePaymentCancel(@PathVariable("order_id") Long orderId) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                             .body(new ApiResponse<>(HttpStatus.BAD_REQUEST, PAYMENT_APPROVE_CANCEL_MESSAGE, null));
    }
}
