package hello.commerce.payment.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class KakaoPayApproveResponseV1 {
    private String aid;
    private String tid;
    private String cid;
    private String partner_order_id;
    private String partner_user_id;
    private String payment_method_type;  // CARD 또는 MONEY 중 하나
    private Amount amount;
    private String itemName;
    private int quantity;
    private LocalDateTime created_at;
    private LocalDateTime approved_at;

    @Getter
    public static class Amount {
        private int total;
        private int tax_free;
        private int vat;
        private int point;
        private int discount;
    }
}
