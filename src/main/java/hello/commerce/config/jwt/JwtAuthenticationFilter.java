package hello.commerce.config.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import hello.commerce.config.auth.PrincipalDetails;
import hello.commerce.user.dto.UserLoginRequestV1;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        setFilterProcessesUrl("/v1/users/login"); // 이 필터가 처리할 로그인 URL 설정
    }

    // 로그인 요청 시 실행되는 메서드
    // /v1/login 요청을 받으면 username, password를 파싱하여 인증을 시도한다.
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        log.info("JwtAuthenticationFilter: 로그인 시도 중");

        try {
            // 1. request Body에서 username과 password를 파싱
            ObjectMapper om = new ObjectMapper();
            UserLoginRequestV1 userLoginRequest = om.readValue(request.getInputStream(), UserLoginRequestV1.class);
            log.info("로그인 요청 : {}", userLoginRequest.getUserId());

            // 2. UsernamePasswordAuthenticationToken 생성, AuthenticationManager에게 인증을 위임할 토큰을 생성
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(userLoginRequest.getUserId(), userLoginRequest.getPassword());

            // 3. AuthenticationManager를 통해 인증 시도
            // PrincipalDetailsService의 loadUserByUsername() 메서드가 실행됨, 인증 성공 시 Authentication 객체를 반환
            Authentication authentication = authenticationManager.authenticate(authenticationToken);

            // PrincipalDetails 객체를 가져와서 로그인된 사용자 정보 확인
            PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
            log.info("로그인 완료: {}", principalDetails.getUsername());

            // authentication 객체가 session 영역에 저장된다. (STATELESS이므로 사실상 의미 없음)
            // 반환된 authentication 객체는 successfulAuthentication 메서드의 파라미터로 전달된다.
            return authentication;

        } catch (IOException e) {
            log.error("로그인 요청 파싱 오류: {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }

    // attemptAuthentication 실행 후 인증이 성공하면 실행되는 메서드
    // JWT 토큰을 생성하여 응답 헤더에 담아 클라이언트에게 반환한다.
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter: 인증 성공");
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        // JWT 토큰 생성, RSA 방식이 아닌 Hash 암호 방식
        String jwtToken = jwtTokenProvider.createToken(principalDetails.getUsername());

        // 응답 헤더에 JWT 토큰 추가
        response.addHeader(jwtTokenProvider.getHeaderString(), jwtTokenProvider.getTokenPrefix() + " " + jwtToken);
        response.getWriter().write("Login successful!"); // 성공 메시지 반환
    }

    // attemptAuthentication 실행 후 인증이 실패하면 실행되는 메서드
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter: 인증 실패: {}", failed.getMessage());
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized
        response.getWriter().write("Authentication Failed: " + failed.getMessage());
    }
}