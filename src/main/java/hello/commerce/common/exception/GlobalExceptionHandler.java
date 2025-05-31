package hello.commerce.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(final BusinessException ex) {
        ErrorCode code = ex.getErrorCode();
        return ResponseEntity
                .status(code.getStatus())
                .body(new ErrorResponse(code.getCode(), code.getMessage(), null));
    }


    // @Valid, @RequestBody 검증 실패
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final MethodArgumentNotValidException ex) {

        FieldError fieldError = ex.getBindingResult().getFieldError();
        String field = fieldError.getField();

        ErrorCode code = switch (field) {
            case "page" -> ErrorCode.INVALID_PAGE;
            case "size" -> ErrorCode.INVALID_SIZE;
            case "quantity" -> ErrorCode.INVALID_ORDER_QUANTITY;
            default -> ErrorCode.INVALID_ARGUMENT;
        };

        return new ErrorResponse(code.getCode(), code.getMessage(), null);
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBindException(final BindException ex) {
        ErrorCode code = ErrorCode.INVALID_ARGUMENT;
        return new ErrorResponse(code.getCode(), code.getMessage(), null);
    }


    // @RequestParam, @PathVariable 타입 불일치
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatchException(final MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();

        ErrorCode code = switch (paramName) {
            case "order_id" -> ErrorCode.INVALID_ORDER_ID_PARAM;
            case "pg_token" -> ErrorCode.INVALID_PG_TOKEN_PARAM;
            default -> ErrorCode.INVALID_ARGUMENT;
        };

        return new ErrorResponse(code.getCode(), code.getMessage(), null);
    }


    // 필수 요청 파라미터 누락 (예: ?size= 빠진 경우)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParam(final MissingServletRequestParameterException ex) {
        String paramName = ex.getParameterName();

        ErrorCode code = switch (paramName) {
            case "page" -> ErrorCode.INVALID_PAGE;
            case "size" -> ErrorCode.INVALID_SIZE;
            case "quantity"     -> ErrorCode.INVALID_ORDER_QUANTITY;
            case "pg_token"     -> ErrorCode.INVALID_PG_TOKEN_PARAM;
            case "order_status" -> ErrorCode.INVALID_ORDER_STATUS;
            default -> ErrorCode.INVALID_ARGUMENT;

            //default -> {
            //            log.warn("Missing unknown request parameter: {}", paramName);
            //            yield ErrorCode.INVALID_ARGUMENT;
            //        }
        };

        return new ErrorResponse(code.getCode(), code.getMessage(), null);
    }

    // 기타 모든 예외
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpected(final Exception ex) {
        log.error("예상치 못한 예외 발생", ex);
        ErrorCode code = ErrorCode.INTERNAL_SERVER_ERROR;
        return new ErrorResponse(code.getCode(), code.getMessage(), null);
    }
}
