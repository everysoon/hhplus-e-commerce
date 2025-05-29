package kr.hhplus.be.server.support.aop.event.idempotent;

public enum IdempotentStatus {
	PROCESSED("1"),
	NOT_PROCESSED("0");
	private final String value;
	IdempotentStatus(String value) {
		this.value = value;
	}
	public String getValue() {
		return value;
	}
}
