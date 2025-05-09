package hello.commerce.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    // 공통
    INVALID_PAGE(100001, "page는 1 이상의 숫자여야 합니다."),
    INVALID_SIZE(100002, "page 당 size는 1에서 20 사이의 숫자여야 합니다."),
    INVALID_ARGUMENT(100003, "유효하지 않은 값입니다."),

    // 300xxx: 주문
    INVALID_ORDER_STATUS(300001, "order_status 값은 INITIAL, PAID, CANCELED 중 하나여야 합니다."),
    INVALID_ORDER_ID_PARAM(300002, "파라미터 타입이 잘못되었습니다."),
    NOT_FOUND_ORDER(300003, "주문 데이터를 찾을 수 없습니다."),

    // 999xxx: 기타
    INTERNAL_SERVER_ERROR(999999, "예상치 못한 오류가 발생했습니다.");


    private final int code;
    private final String message;

}
