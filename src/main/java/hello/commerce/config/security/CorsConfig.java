package hello.commerce.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowCredentials(true); // 내 서버가 응답을 할 때 json을 자바스크립트에서 처리할 수 있게 할지를 설정하는 것
        config.addAllowedOrigin("http://localhost:5173");

        config.addAllowedHeader("Authorization"); // JWT 인증을 위한 헤더 허용
        config.addAllowedHeader("Content-Type"); // JSON 데이터를 위한 헤더 허용

        config.addAllowedMethod("GET");
        config.addAllowedMethod("POST");
        config.addAllowedMethod("PUT");
        config.addAllowedMethod("DELETE");

        config.addExposedHeader("Authorization");  // 브라우저가 Authorization 헤더를 읽을 수 있도록 노출
        source.registerCorsConfiguration("/v1/**", config);

        return new CorsFilter(source);
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//
//        config.setAllowCredentials(false);
//        // addAllowedOrigin("*") 대신 addAllowedOriginPatterns("*") 사용
//        config.addAllowedOriginPatterns("*");
//        config.addAllowedOrigin("*");
//        config.addAllowedHeader("*");
//        config.addAllowedMethod("*");
//        config.addExposedHeader("Authorization");
//        source.registerCorsConfiguration("/v1/**", config);
//
//        return new CorsFilter(source);
//    }

}