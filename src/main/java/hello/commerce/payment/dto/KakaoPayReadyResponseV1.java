package hello.commerce.payment.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
public class KakaoPayReadyResponseV1 {
    private String tid;
    private String next_redirect_pc_url;
    private LocalDateTime createdAt;
}
