package hello.commerce.user;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

//    @InjectMocks
//    UserService userService;

    // --- 사용자 정보 조회 테스트: getUserById() ---

    @Test
    @Disabled
    @DisplayName("사용자_조회_성공")
    void getUserById_success() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("사용자_조회_실패_사용자_없음")
    void getUserById_fail_with_user_not_found() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("회원가입_성공")
    void joinUser_success() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("회원가입_실패_중복된_아이디")
    void joinUser_fail_with_duplicate_userId() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("회원가입_실패_중복된_이메일")
    void joinUser_fail_with_duplicate_email() {
        throw new UnsupportedOperationException();
    }

    // --- 로그인 테스트: loginUser() ---

    @Test
    @Disabled
    @DisplayName("로그인_성공")
    void loginUser_success() {
        throw new UnsupportedOperationException();
    }

    @Test
    @Disabled
    @DisplayName("로그인_실패_아이디_또는_비밀번호_불일치")
    void loginUser_fail_with_invalid_credentials() {
        throw new UnsupportedOperationException();
    }

}
