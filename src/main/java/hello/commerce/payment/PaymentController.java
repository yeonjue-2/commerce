package hello.commerce.payment;


import hello.commerce.common.response.ApiResponse;
import hello.commerce.payment.dto.KakaoPayReadyResponseV1;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @Value("${frontend.vue.base-url}")
    private String frontendBaseUrl;

    public static final String PAYMENT_APPROVE_RESULT_URI = "/payments/result?status=";

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
        URI redirectUri = URI.create(getRedirectURI("success"));
        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
    }

    // 2. 결제 실패

    @GetMapping("/v1/payments/orders/{order_id}/fail")
    public ResponseEntity<Void> handlePaymentFailure(@PathVariable("order_id") Long orderId) {
        URI redirectUri = URI.create(getRedirectURI("fail"));
        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
    }
    // 3. 결제 취소

    @GetMapping("/v1/payments/orders/{order_id}/cancel")
    public ResponseEntity<Void> handlePaymentCancel(@PathVariable("order_id") Long orderId) {
        URI redirectUri = URI.create(getRedirectURI("cancel"));
        return ResponseEntity.status(HttpStatus.FOUND).location(redirectUri).build();
    }

    private String getRedirectURI(String result) {
        return frontendBaseUrl + PAYMENT_APPROVE_RESULT_URI + result;
    }
}
