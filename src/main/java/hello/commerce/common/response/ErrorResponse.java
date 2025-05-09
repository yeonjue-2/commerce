package hello.commerce.common.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private int error_code;
    private String error_message;
    private Map<String, Object> error_data;

    public ErrorResponse(int errorCode, String message) {
        this.error_code = errorCode;
        this.error_message = message;
        this.error_data = null;
    }
}
