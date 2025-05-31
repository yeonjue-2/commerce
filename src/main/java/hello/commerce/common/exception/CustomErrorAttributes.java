package hello.commerce.common.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {
    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Throwable error = getError(webRequest);

        ErrorCode code = resolveErrorCode(error);

        Map<String, Object> attributes = new LinkedHashMap<>();
        attributes.put("timestamp", LocalDateTime.now());
        attributes.put("status", code.getStatus().value());
        attributes.put("code", code.getCode());
        attributes.put("message", code.getMessage());

        return attributes;
    }

    private ErrorCode resolveErrorCode(Throwable error) {
        if (error instanceof HttpRequestMethodNotSupportedException) {
            return ErrorCode.METHOD_NOT_ALLOWED;
        }

        // 기본값은 404
        return ErrorCode.NOT_FOUND_RESOURCE;
    }
}

