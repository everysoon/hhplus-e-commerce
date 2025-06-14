package kr.hhplus.be.server.domain.coupon;

public enum CouponStatus {
//    PENDING, // 쿠폰발급만 하고, 사용자에게 발급이 되지않음
    ISSUED, // 쿠폰이 사용자에게 발급된 상태. 사용자가 쿠폰을 받았지만 아직 사용하지 않은 상태
    USED, // 사용자가 쿠폰을 사용한 상태. 쿠폰이 실제로 주문 시에 사용되었음을 나타냄
    EXPIRED, // 쿠폰이 유효 기간을 지나 만료된 상태. 사용자가 쿠폰을 사용하지 않고 만료된 경우
    REVOKED // 환불 - 쿠폰이 관리자나 시스템에 의해 취소된 상태. 예를 들어, 쿠폰이 부정하게 발급되었거나 특정 조건을 충족하지 않는 경우
}
