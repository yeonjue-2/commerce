package hello.commerce.payment.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class KakaoPayReadyResponseV1 {
    private String tid;

    @JsonProperty("next_redirect_pc_url")
    private String nextRedirectPcUrl;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;
}
