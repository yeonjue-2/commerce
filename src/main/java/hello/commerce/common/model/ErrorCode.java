package hello.commerce.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통
    INVALID_PAGE            (100_001, "page는 1 이상의 숫자여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_SIZE            (100_002, "page 당 size는 1에서 20 사이의 숫자여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_ARGUMENT        (100_003, "유효하지 않은 값입니다.", HttpStatus.BAD_REQUEST),

    // 상품
    NOT_FOUND_PRODUCT       (200_001, "상품 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

    // 300xxx: 주문
    INVALID_ORDER_STATUS    (300_001, "order_status 값은 INITIAL, PAID, CANCELED 중 하나여야 합니다.", HttpStatus.BAD_REQUEST),
    INVALID_ORDER_ID_PARAM  (300_002, "파라미터 타입이 잘못되었습니다.", HttpStatus.BAD_REQUEST),
    NOT_FOUND_ORDER         (300_003, "주문 데이터를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    INVALID_ORDER_QUANTITY  (300_005, "요청 파라미터가 유효하지 않습니다. 수량은 1 이상이어야 합니다.", HttpStatus.BAD_REQUEST),
    INSUFFICIENT_STOCK      (300_006, "상품의 재고가 부족합니다.", HttpStatus.CONFLICT),

    // 999xxx: 기타
    INTERNAL_SERVER_ERROR   (999_999, "예상치 못한 오류가 발생했습니다.", HttpStatus.BAD_REQUEST);


    private final int code;
    private final String message;
    private final HttpStatus status;
}
