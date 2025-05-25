package hello.commerce.payment.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class KakaoPayReadyRequestV1 {

    @NotNull
    private String cid;

    @NotNull
    private String partnerOrderId;

    @NotNull
    private String partnerUserId;

    @NotNull
    private String itemName;

    @NotNull @Min(1)
    private int quantity;

    @NotNull @PositiveOrZero
    private int totalAmount;

    @NotNull
    private int taxFreeAmount;

    @NotNull
    private String approvalUrl;

    @NotNull
    private String cancelUrl;

    @NotNull
    private String failUrl;
}
