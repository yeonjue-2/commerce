package hello.commerce.config;

import hello.commerce.common.properties.KakaoPayProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.List;


@Configuration
@EnableConfigurationProperties(KakaoPayProperties.class)
public class KakaoPayConfig {

    @Bean
    public RestTemplate kakaoRestTemplate(KakaoPayProperties properties) {
        RestTemplate restTemplate = new RestTemplate();

        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(properties.getBaseUrl()));

        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().set("Authorization", "SECRET_KEY " + properties.getSecretKey());
            request.getHeaders().setAccept(List.of(MediaType.APPLICATION_JSON));
            request.getHeaders().setContentType(MediaType.APPLICATION_JSON);
            return execution.execute(request, body);
        });

        return restTemplate;
    }
}
