package kr.hhplus.be.server.support.aop.lock;

import org.redisson.api.RLock;

public interface LockRepository {
	RLock getLock(String lockKey);
}
