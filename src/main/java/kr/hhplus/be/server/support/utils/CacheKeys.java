package kr.hhplus.be.server.support.utils;

public enum CacheKeys {
	POPULAR_PRODUCT("cache:popular:products:%s"),
	COUPON_ISSUED_USER("cache:coupon:%s:users:issued:%s"),
	COUPON_ISSUE_REQUEST_USER("cache:coupon:%s:users:request:%d"),
	COUPON_STOCK("cache:coupon:stock:%s"),
	COUPON_STREAM("coupon:issue:stream"),
	PRODUCT_UNION("cache:popular:products:union:%s_to_%s");
	private final String key;
	CacheKeys(String key) {
		this.key= key;
	}
	public String getKey(String str){
		return String.format(this.key,str);
	}
	public String getKey(Long str){
		return String.format(this.key,str);
	}
	public String getKey(Object[] objects){
		return String.format(this.key,objects);
	}
	public String getKey(){
		return this.key;
	}
}
