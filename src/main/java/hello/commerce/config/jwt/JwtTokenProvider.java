package hello.commerce.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
@Getter
@RequiredArgsConstructor
@Slf4j
public class JwtTokenProvider {

    private final UserDetailsService userDetailsService;

    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration-time}")
    private long expirationTime;

    @Value("${jwt.token-prefix}")
    private String tokenPrefix;

    @Value("${jwt.header-string}")
    private String headerString;

    // JWT 토큰 생성
    public String createToken(String username) {
        // HMAC512 알고리즘을 사용하여 JWT 토큰 생성
        return JWT.create()
                .withSubject(username) // 토큰의 주체 (사용자 이름)
                .withExpiresAt(new Date(System.currentTimeMillis() + expirationTime)) // 토큰 만료 시간 설정
                .withClaim("username", username) // 사용자 이름을 클레임에 추가
                .sign(Algorithm.HMAC512(secretKey)); // secret 키로 서명
    }

    public String generateToken(Authentication authentication) {
        String username = authentication.getName();
        Date now = new Date();
        Date validity = new Date(now.getTime() + expirationTime);

        return JWT.create()
                .withSubject(username)
                .withIssuedAt(now)
                .withExpiresAt(validity)
                .sign(Algorithm.HMAC256(secretKey));
    }

    // 토큰에서 인증 정보 조회
    public Authentication getAuthentication(String token) {
        DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC256(secretKey)).build().verify(token);
        String username = decodedJWT.getSubject();
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }


    // JWT 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            // 토큰 서명 및 만료 시간 검증
            JWT.require(Algorithm.HMAC512(secretKey)).build().verify(token);
            return true;
        } catch (TokenExpiredException e) {
            log.error("JWT 토큰이 만료되었습니다: {}", e.getMessage());
            return false;
        } catch (JWTVerificationException e) {
            log.error("유효하지 않은 JWT 토큰입니다: {}", e.getMessage());
            return false;
        } catch (Exception e) {
            log.error("JWT 토큰 검증 중 알 수 없는 오류 발생: {}", e.getMessage());
            return false;
        }
    }

    // JWT 토큰에서 사용자 이름 추출
    public String getUsernameFromToken(String token) {
        return JWT.require(Algorithm.HMAC512(secretKey))
                .build()
                .verify(token)
                .getClaim("username")
                .asString();
    }
}
