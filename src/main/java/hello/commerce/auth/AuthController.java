package hello.commerce.auth;

import hello.commerce.auth.dto.AuthTokenRefreshRequestV1;
import hello.commerce.user.dto.UserLoginResponseV1;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping
public class AuthController {

    @PostMapping("/v1/auth/token/refresh")
    public UserLoginResponseV1 refreshToken(@RequestBody AuthTokenRefreshRequestV1 request) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @GetMapping("/v1/auth/{provider}/login")
    public ResponseEntity<Void> oauthLogin(@PathVariable String provider) {
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @PostMapping("/v1/auth/logout")
    public ResponseEntity<Void> logout() {
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
