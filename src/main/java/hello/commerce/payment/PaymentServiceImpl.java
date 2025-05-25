package hello.commerce.payment;

import hello.commerce.payment.dto.KakaoPayReadyResponseV1;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public KakaoPayReadyResponseV1 prepareKakaoPay(Long orderId) {
        return null;
    }
}
