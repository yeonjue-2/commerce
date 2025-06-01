package hello.commerce.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {
    private int code;
    private String message;
    private T data;

    public ApiResponse(HttpStatus status, String message, T data) {
        this.code = status.value();  // 상태 코드를 명시적으로 가져옴
        this.message = message;
        this.data = data;
    }
}