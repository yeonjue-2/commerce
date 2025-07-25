package hello.commerce.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 100xxx: 공통
    INVALID_PAGE            (100_001, "page는 1 이상의 숫자여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_SIZE            (100_002, "page 당 size는 1에서 20 사이의 숫자여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_ARGUMENT        (100_003, "유효하지 않은 값입니다."         , HttpStatus.BAD_REQUEST),
    NOT_FOUND_RESOURCE      (100_097, "요청한 리소스를 찾을 수 없습니다." , HttpStatus.NOT_FOUND),
    METHOD_NOT_ALLOWED      (100_098, "지원하지 않는 HTTP 메서드입니다." , HttpStatus.METHOD_NOT_ALLOWED),

    // 200xxx: 상품
    NOT_FOUND_PRODUCT       (200_001, "상품 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    LOCK_TIMEOUT_PRODUCT    (200_002, "다른 상품 주문 요청이 처리 중입니다. 잠시 후 다시 시도해주세요.", HttpStatus.CONFLICT),

    // 300xxx: 주문
    INVALID_ORDER_STATUS    (300_001, "order_status 값은 INITIAL, PAID, CANCELED 중 하나여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_ID_PARAM  (300_002, "파라미터 타입이 잘못되었습니다."                        , HttpStatus.BAD_REQUEST),
    NOT_FOUND_ORDER         (300_003, "주문 데이터를 찾을 수 없습니다."                        , HttpStatus.NOT_FOUND),
    INVALID_ORDER_QUANTITY  (300_004, "요청 파라미터가 유효하지 않습니다. 수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK      (300_005, "상품의 재고가 부족합니다."                             , HttpStatus.CONFLICT),
    INVALID_ORDER_STATUS_TRANSITION  (300_006, "결제 준비는 INITIAL 상태의 주문에서만 가능합니다.", HttpStatus.BAD_REQUEST),
    LOCK_TIMEOUT_ORDER      (300_006, "다른 주문 요청이 처리 중입니다. 잠시 후 다시 시도해주세요."   , HttpStatus.CONFLICT),

    // 400xxx: 결제
    NOT_FOUND_PAYMENT       (400_001, "결제 데이터를 찾을 수 없습니다."           , HttpStatus.NOT_FOUND),
    INVALID_PG_TOKEN_PARAM  (400_003, "유효하지 않은 token 값입니다."           , HttpStatus.BAD_REQUEST ),
    ALREADY_PREPARED_PAYMENT(400_004, "이미 결제정보가 존재합니다."              , HttpStatus.CONFLICT),

    // 500xxx: 외부 API 관련
    KAKAO_API_ERROR         (500_001, "카카오페이 API 호출 중 오류가 발생하였습니다.", HttpStatus.BAD_GATEWAY),

    // 999xxx: 기타
    INTERNAL_SERVER_ERROR   (999_999, "예상치 못한 오류가 발생했습니다."          , HttpStatus.BAD_REQUEST);


    private final int code;
    private final String message;
    private final HttpStatus status;
}
