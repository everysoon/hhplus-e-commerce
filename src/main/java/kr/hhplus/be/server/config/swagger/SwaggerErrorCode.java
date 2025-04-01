package kr.hhplus.be.server.config.swagger;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.web.client.HttpClientErrorException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Getter
@AllArgsConstructor
public enum SwaggerErrorCode {
    // common
    HANDLE_ERROR(BAD_REQUEST.value(),"-101","서비스에서 오류가 발생했습니다."),
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
    // lock
    LOCK_ACQUISITION_FAIL(BAD_REQUEST.value(),"-601","락을 획득하는데 실패했습니다. 다시 시도해주세요.");

    private int statusCode;
    private String processCode;
    private String message;
}
