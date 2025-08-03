package hello.commerce.auth.dto;

import lombok.Getter;

@Getter
public class AuthTokenRefreshRequestV1 {
    private String refreshToken;
}
