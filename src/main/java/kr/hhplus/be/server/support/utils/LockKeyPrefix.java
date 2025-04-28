package kr.hhplus.be.server.support.utils;

public enum LockKeyPrefix {
	PRODUCT("lock:product:"),
	USER_POINT("lock:point:"),
	COUPON("lock:coupon:"),
	USER_COUPON("lock:user:coupon:");

	private final String prefix;

	LockKeyPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String createKey(Long id){
		return prefix + id;
	}
	public String createKey(String id){
		return prefix + id;
	}
}
