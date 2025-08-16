package hello.commerce.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;

@AllArgsConstructor
@Builder
public class UserLoginResponseV1 {
    private String accessToken;
    private String refreshToken;
}
