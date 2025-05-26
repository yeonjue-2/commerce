package hello.commerce.config;

import hello.commerce.common.properties.KakaoPayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableConfigurationProperties(KakaoPayProperties.class)
public class KakaoPayConfig {

    @Bean
    public WebClient kakaoWebClient(KakaoPayProperties properties) {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("Authorization", "SECRET_KEY " + properties.getSecretKey())
                .build();
    }
}
