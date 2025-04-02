package kr.hhplus.be.server.utils;

import java.math.BigDecimal;

public class TestUtils {
    public static final String SUCCESS_MSG = "요청이 성공했습니다.";
    public static final String SUCCESS_PROCESS_CODE = "1";

    public static final BigDecimal defaultPoint = BigDecimal.ZERO;
    public static final Integer defaultQuantity = 10;
    public static final String defaultCouponDesc = "TEST COUPON";
    public static final BigDecimal defaultCouponDiscount = new BigDecimal(1000);
    public static final BigDecimal defaultProductPrice = new BigDecimal(5000);
    public static final String defaultProductName = "다이소 장난감 ";
    public static final String defaultProductDesc ="유아용 다이소 자동차 장난감 ";
    public static final Integer defaultCouponRemainingStock = 10;
    public static final Integer defaultProductStock = 10;

    public static final Long userId = 1L;
    public static final String userName = "minsoon";
    public static final String email = "soonforjoy@gmail.com";
    public static final String address = "Guro,Seoul";
}
