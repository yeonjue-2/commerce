package hello.commerce.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

@TestConfiguration
public class TestConfig {

    // 빈만 등록하지 않으면 Auditing 작동 안 함
    // 또는 필요 시 Dummy 구현 제공
    @Bean
    public AuditorAware<Long> auditorAware() {
        return Optional::empty;
    }
}