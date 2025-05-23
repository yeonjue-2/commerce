package hello.commerce.order.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderRequestV1 {
    @NotNull
    private Long userId;

    @NotNull
    private Long productId;

    @Min(1)
    private int quantity;
}
