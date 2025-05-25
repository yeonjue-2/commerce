package hello.commerce.common.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties(prefix = "kakao")  // yml에 정의된 properties 값 자동 바인딩
@Getter
public class KakaoPayProperties {
    private String cid;
    private String secretKey;
    private String baseUrl;
    private String readyUrl;
    private String approveUrl;
    private String baseRedirectUrl;
    private int taxFreeAmount;
}
