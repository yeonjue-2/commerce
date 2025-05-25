package hello.commerce.payment;

import hello.commerce.payment.dto.KakaoPayReadyResponseV1;

public interface PaymentService {
    KakaoPayReadyResponseV1 prepareKakaoPay(Long orderId);
    void approveKakaoPay(Long orderId, String pgToken);
}
