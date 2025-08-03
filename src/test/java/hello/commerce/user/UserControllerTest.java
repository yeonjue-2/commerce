package hello.commerce.user;

import hello.commerce.config.ControllerTestSupport;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UserControllerTest extends ControllerTestSupport {

    @Test
    @Disabled
    @DisplayName("회원가입_성공")
    void joinUser_success() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("회원가입_실패_필수값_누락")
    void joinUser_fail_with_invalid_input() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("회원가입_실패_중복된_아이디")
    void joinUser_fail_with_duplicate_userId() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("회원가입_실패_중복된_이메일")
    void joinUser_fail_with_duplicate_email() throws Exception {
        throw new UnsupportedOperationException();
    }

    // --- 로그인 API 테스트: POST /v1/users/login ---

    @Test
    @Disabled
    @DisplayName("로그인_성공")
    void loginUser_success() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("로그인_실패_유효하지_않은_입력값")
    void loginUser_fail_with_invalid_input() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("로그인_실패_아이디_또는_비밀번호_불일치")
    void loginUser_fail_with_invalid_credentials() throws Exception {
        throw new UnsupportedOperationException();
    }

    // --- 사용자 정보 조회 API 테스트: GET /v1/users/{user_id} ---

    @Test
    @Disabled
    @DisplayName("사용자_정보_조회_성공")
    void getUserInfo_success() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("사용자_정보_조회_실패_인증_없음")
    void getUserInfo_fail_without_token() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("사용자_정보_조회_실패_권한_없음")
    void getUserInfo_fail_with_unauthorized_token() throws Exception {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("사용자_정보_조회_실패_사용자_없음")
    void getUserInfo_fail_with_not_found_user() throws Exception {
        throw new UnsupportedOperationException();
    }
}
