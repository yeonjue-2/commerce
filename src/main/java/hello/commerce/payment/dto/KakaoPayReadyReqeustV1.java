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
public class KakaoPayReadyReqeustV1 {

    @NotNull
    private String cid;

    @NotNull
    private String partner_order_id;

    @NotNull
    private String partner_user_id;

    @NotNull
    private String item_name;

    @NotNull @Min(1)
    private int quantity;

    @NotNull @PositiveOrZero
    private int total_amount;

    @NotNull
    private int tax_free_amount;

    @NotNull
    private String approval_url;

    @NotNull
    private String cancel_url;

    @NotNull
    private String fail_url;
}
