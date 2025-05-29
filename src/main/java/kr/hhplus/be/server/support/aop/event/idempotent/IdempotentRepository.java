package kr.hhplus.be.server.support.aop.event.idempotent;

public interface IdempotentRepository {
	boolean isAlreadyProcessed(String key);
	void markProcessed(String key, long ttlSeconds);
	void remove(String key);
}
