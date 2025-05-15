package kr.hhplus.be.server.support.utils;

public enum CacheKeys {
	POPULAR_PRODUCT("cache:popular:products:%s"),
	PRODUCT_UNION_KEY("cache:popular:products:union:%s_to_%s_top_%d");
	private final String key;
	CacheKeys(String key) {
		this.key= key;
	}
	public String getKey(String str){
		return String.format(this.key,str);
	}
	public String getKey(Object[] objects){
		return String.format(this.key,objects);
	}
	public String getKey(){
		return this.key;
	}
}
