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

    public String getApprovalRedirectUrl(Long orderId) {
        return baseRedirectUrl + "/" + orderId + "/approve";
    }

    public String getCancelRedirectUrl(Long orderId) {
        return baseRedirectUrl + "/" + orderId + "/cancel";
    }

    public String getFailRedirectUrl(Long orderId) {
        return baseRedirectUrl + "/" + orderId + "/fail";
    }
}
