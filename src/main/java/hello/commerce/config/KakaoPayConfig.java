package hello.commerce.config;

import hello.commerce.common.properties.KakaoPayProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class KakaoPayConfig {

    @Bean
    public WebClient kakaoWebClient(KakaoPayProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.getSecretKey())
                .defaultHeader("Authorization", "KakaoAK " + properties.getSecretKey())
                .defaultHeader("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .build();
    }
}
