package hello.commerce.auth;

import hello.commerce.config.ControllerTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class AuthControllerTest extends ControllerTestSupport {

    // --- 토큰 갱신 API 테스트: POST /v1/auth/token/refresh ---

    @Test
    @Disabled
    @DisplayName("토큰_갱신_성공")
    void refreshToken_success() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("토큰_갱신_실패_유효하지_않은_리프레시_토큰")
    void refreshToken_fail_with_invalid_token() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("토큰_갱신_실패_만료된_리프레시_토큰")
    void refreshToken_fail_with_expired_token() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("토큰_갱신_실패_요청_본문_누락")
    void refreshToken_fail_with_missing_body() throws Exception {
        throw new UnsupportedOperationException();
    }

    // --- 로그아웃 API 테스트: POST /v1/auth/logout ---

    @Test
    @Disabled
    @DisplayName("로그아웃_성공")
    void logout_success() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("로그아웃_실패_인증_헤더_누락")
    void logout_fail_without_authorization_header() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("로그아웃_실패_만료된_액세스_토큰")
    void logout_fail_with_expired_accessToken() throws Exception {
        throw new UnsupportedOperationException();
    }

    // --- OAuth 로그인 시작 API 테스트: GET /v1/auth/{provider}/login ---

    @Test
    @Disabled
    @DisplayName("OAuth_로그인_시작_성공_카카오")
    void oauthLogin_success_kakao() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("OAuth_로그인_시작_실패_지원하지_않는_제공자")
    void oauthLogin_fail_with_unsupported_provider() throws Exception {
        throw new UnsupportedOperationException();
    }
}

