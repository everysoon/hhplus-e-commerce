package kr.hhplus.be.server.domain.payment;

public enum PaymentStatus {
    PENDING,
    COMPLETED,
    FAILED,
    REFUNDED // 환불
}
