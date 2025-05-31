package hello.commerce.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class KakaoPayApproveResponseV1 {
    private String aid;
    private String tid;
    private String cid;
    @JsonProperty("partner_order_id")
    private String partnerOrderId;
    @JsonProperty("partner_user_id")
    private String partnerUserId;
    @JsonProperty("partner_method_type")
    private String paymentMethodType;  // CARD 또는 MONEY 중 하나
    private Amount amount;
    @JsonProperty("item_name")
    private String itemName;
    private int quantity;
    @JsonProperty("created_at")
    private LocalDateTime createdAt;
    @JsonProperty("approved_at")
    private LocalDateTime approvedAt;

    @Getter
    public static class Amount {
        private int total;
        private int taxFree;
        private int vat;
        private int point;
        private int discount;
    }
}
