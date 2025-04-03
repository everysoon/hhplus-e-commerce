package kr.hhplus.be.server.config.swagger;

import lombok.AllArgsConstructor;
import lombok.Getter;

import static org.springframework.http.HttpStatus.*;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // common
    HANDLE_ERROR(BAD_REQUEST.value(),"-101","서비스에서 오류가 발생했습니다."),
    CLIENT_INPUT_ERROR(BAD_REQUEST.value(),"-102","매개변수 값을 확인해주세요."),
    INVALID_CLIENT_VALUE(BAD_REQUEST.value(),"-103","변수 형태를 확인해주세요."),

    // user
    INVALID_USER(BAD_REQUEST.value(),"-201","유효하지 않은 사용자입니다."),
    NOT_EXIST_USER(BAD_REQUEST.value(),"-202","사용자가 존재하지 않습니다."),

    // product
    OUT_OF_STOCK(BAD_REQUEST.value(),"-301","상품이 품절 상태 입니다."),
    NOT_EXIST_PRODUCT(BAD_REQUEST.value(),"-302","해당 상품이 존재하지 않습니다."),

    // coupon
    NOT_EXIST_COUPON(BAD_REQUEST.value(),"-401","유효하지 않은 쿠폰입니다."),
    COUPON_SOLD_OUT(BAD_REQUEST.value(),"-402","선착순 쿠폰이 마감되었습니다."),

    // point
    INSUFFICIENT_POINTS(BAD_REQUEST.value(),"-501","사용 가능한 포인트가 부족합니다."),
	INVALID_CHARGE_AMOUNT(BAD_REQUEST.value(),"-502","충전 금액은 0보다 커야합니다."),

    // lock
    LOCK_ACQUISITION_FAIL(BAD_REQUEST.value(),"-601","락을 획득하는데 실패했습니다. 다시 시도해주세요."),

    CUSTOM_METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED.value(), "-1001","지원하지 않은 요청입니다. 요청 정보를 다시 확인해 주시기 바랍니다."),
    CUSTOM_INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "-1002","예상하지 않은 에러가 발생하였습니다. 관리자에게 문의해 주세요."),
	;
    private final int statusCode;
    private final String processCode;
    private final String message;

}
