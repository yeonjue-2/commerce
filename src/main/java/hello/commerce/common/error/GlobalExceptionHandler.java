package hello.commerce.common.error;

import hello.commerce.common.model.BusinessException;
import hello.commerce.common.model.ErrorCode;
import hello.commerce.common.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex) {
        ErrorCode code = ex.getErrorCode();
        return ResponseEntity
                .status(code.getStatus())
                .body(new ErrorResponse(code.getCode(), code.getMessage(), null));
    }


    // @ModelAttribute는 원래 BindException을 발생시켜야 맞지만,
    // 컨트롤러 파라미터에서 @Valid가 붙으면 상황에 따라 MethodArgumentNotValidException이 발생할 수 있어 함께처리
    // 1. @Valid, @RequestBody 검증 실패
    @ExceptionHandler({BindException.class, MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(Exception ex) {

        BindingResult bindingResult = ex instanceof BindException
                ? ((BindException) ex).getBindingResult()
                : ((MethodArgumentNotValidException) ex).getBindingResult();

        FieldError fieldError = bindingResult.getFieldError();
        String field = fieldError.getField();

        ErrorCode code = switch (field) {
            case "page" -> ErrorCode.INVALID_PAGE;
            case "size" -> ErrorCode.INVALID_SIZE;
            default -> ErrorCode.INVALID_ARGUMENT;
        };

        return new ErrorResponse(code.getCode(), code.getMessage(), null);
    }


    // 2. @RequestParam, @PathVariable 타입 불일치
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String paramName = ex.getName();

        ErrorCode code = switch (paramName) {
            case "order_id" -> ErrorCode.INVALID_ORDER_ID_PARAM;
            default -> ErrorCode.INVALID_ARGUMENT;
        };

        return new ErrorResponse(code.getCode(), code.getMessage(), null);
    }

    // 3. 필수 요청 파라미터 누락 (예: ?size= 빠진 경우)
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingParam(MissingServletRequestParameterException ex) {
        return new ErrorResponse(100098, "필수 요청 파라미터가 없습니다: " + ex.getParameterName(), null);
    }

    // 4. 요청 URL이 없음 (예: 잘못된 경로 요청)
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNoHandler(NoHandlerFoundException ex) {
        return new ErrorResponse(100097, "요청한 리소스를 찾을 수 없습니다.", null);
    }

    // 2. 기타 모든 예외
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleUnexpected(Exception ex) {
        ErrorCode code = ErrorCode.INTERNAL_SERVER_ERROR;
        return new ErrorResponse(code.getCode(), code.getMessage(), null);
    }
}
