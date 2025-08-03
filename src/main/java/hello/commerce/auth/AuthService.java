package hello.commerce.auth;

import hello.commerce.auth.dto.AuthTokenRefreshRequestV1;
import hello.commerce.user.dto.UserLoginResponseV1;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    UserLoginResponseV1 refresh(AuthTokenRefreshRequestV1 request);

    void oauthLogin(String provider);

    void logout();
}
