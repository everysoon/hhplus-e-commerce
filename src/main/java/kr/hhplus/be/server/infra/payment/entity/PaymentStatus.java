package kr.hhplus.be.server.infra.payment.entity;

public enum PaymentStatus {
	PENDING, // 결제 대기
	COMPLETED, // 결제 완료
	FAIL,
	CANCELED, // 결제 취소
}
