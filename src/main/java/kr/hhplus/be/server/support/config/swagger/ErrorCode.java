package kr.hhplus.be.server.support.config.swagger;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.METHOD_NOT_ALLOWED;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    // common
    HANDLE_ERROR(BAD_REQUEST.value(),"서비스에서 오류가 발생했습니다."),
    CLIENT_INPUT_ERROR(BAD_REQUEST.value(),"매개변수 값을 확인해주세요."),
    INVALID_CLIENT_VALUE(BAD_REQUEST.value(),"변수 형태를 확인해주세요."),

    // user
    INVALID_USER(BAD_REQUEST.value(),"유효하지 않은 사용자입니다."),
    NOT_EXIST_USER(BAD_REQUEST.value(),"사용자가 존재하지 않습니다."),

    // product
    OUT_OF_STOCK(BAD_REQUEST.value(),"상품이 품절 상태 입니다."),
    NOT_EXIST_PRODUCT(BAD_REQUEST.value(),"해당 상품이 존재하지 않습니다."),
	INVALID_QUANTITY(BAD_REQUEST.value(),"잘못된 수량입니다."),
	INVALID_CATEGORY(BAD_REQUEST.value(),"잘못된 상품 카테고리입니다."),
	INVALID_SORTED(BAD_REQUEST.value(),"잘못된 정렬순입니다."),
	INVALID_SORTED_BY(BAD_REQUEST.value(),"잘못된 정렬값입니다."),

    // coupon
    NOT_EXIST_COUPON(BAD_REQUEST.value(),"해당 쿠폰을 찾을 수 없습니다."),
    USED_COUPON(BAD_REQUEST.value(),"이미 사용된 쿠폰이므로 사용할 수 없습니다."),
    REVOKED_COUPON(BAD_REQUEST.value(),"관리자나 시스템에 의해 취소된 쿠폰 입니다."),
    COUPON_SOLD_OUT(BAD_REQUEST.value(),"선착순 마감으로 쿠폰 재고가 존재하지않습니다."),
	INVALID_COUPON(BAD_REQUEST.value(),"유효하지 않은 쿠폰입니다."),
	INVALID_COUPON_ID(BAD_REQUEST.value(),"쿠폰ID가 UUID형식이 아닙니다."),
	EXPIRED_COUPON(BAD_REQUEST.value(),"사용기간이 지난 쿠폰입니다."),
	INVALID_EXPIRED_COUPON_DATE(BAD_REQUEST.value(),"쿠폰 만료일이 발행일 이전입니다."),
	COUPON_ISSUED_FAIL(BAD_REQUEST.value(),"쿠폰 발급에 대한 동시성 에러가 발생했습니다."),
	DUPLICATE_COUPON_CLAIM(BAD_REQUEST.value(),"이미 쿠폰을 받은 발급자 입니다."),
	INVALID_USER_COUPON(BAD_REQUEST.value(),"해당 쿠폰이 사용자 권한이 아닙니다."),
	INVALID_COUPON_QUANTITY(BAD_REQUEST.value(),"발행 수량은 남은 수량보다 적을 수 없습니다."),

    // point
    INSUFFICIENT_POINTS(BAD_REQUEST.value(),"사용 가능한 포인트가 부족합니다."),
	INVALID_CHARGE_AMOUNT(BAD_REQUEST.value(),"충전 금액은 0보다 커야합니다."),
	NOT_EXIST_POINT_BY_USER_ID(BAD_REQUEST.value(),"해당 유저 ID로 포인트 정보를 찾지 못했습니다."),

    // lock
    LOCK_ACQUISITION_FAIL(BAD_REQUEST.value(),"락을 획득하는데 실패했습니다. 다시 시도해주세요."),

	// order
	NOT_EXIST_ORDER_ITEM(BAD_REQUEST.value(),"주문 상품이 존재하지 않습니다."),
    CUSTOM_METHOD_NOT_ALLOWED(METHOD_NOT_ALLOWED.value(), "지원하지 않은 요청입니다. 요청 정보를 다시 확인해 주시기 바랍니다."),
    CUSTOM_INTERNAL_SERVER_ERROR(INTERNAL_SERVER_ERROR.value(),"예상하지 않은 에러가 발생하였습니다. 관리자에게 문의해 주세요."),
	NOT_EXIST_ORDER(INTERNAL_SERVER_ERROR.value(),"해당 주문ID의 주문내역이 존재하지 않습니다."),
	UNAUTHORIZED_ORDER_ACCESS(INTERNAL_SERVER_ERROR.value(),"사용자의 주문내역이 아닙니다."),

	// payment
	PAYMENT_FAIL(BAD_REQUEST.value(),"결제가 실패했습니다."),
	NOT_EXIST_PAYMENT_BY_ORDER(BAD_REQUEST.value(),"해당 주문건의 결제내역을 찾을 수 없습니다."),
	NOT_EXIST_PAYMENT_BY_TX_ID(BAD_REQUEST.value(),"해당 트랜잭션 ID로 결제 내역을 조회할 수 없습니다."),
	EXTERNAL_PAYMENT_API_ERROR(BAD_REQUEST.value(),"외부 결제 API에서 오류가 발생했습니다.")
	;
    private final int statusCode;
    private final String message;

}

