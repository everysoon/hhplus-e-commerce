package kr.hhplus.be.server.support.aop.lock;

public enum LockKeyPrefix {
	PRODUCT("lock:product:%d"),
	USER_POINT("lock:point:%d"),
	COUPON("lock:coupon:%s"),
	ORDER("lock:order:%d"),
	ORDER_CANCEL("lock:order:cancel:%d"),
	USER_COUPON("lock:user:%d:coupon:%s");

	private final String prefix;

	LockKeyPrefix(String prefix) {
		this.prefix = prefix;
	}
	public String createKey(Long id){
		return String.format(prefix,id);
	}
	public String createKey(Object[] params){
		return String.format(prefix, params);
	}
	public String createKey(String id){
		return String.format(prefix,id);
	}
}
