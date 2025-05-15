package kr.hhplus.be.server.infra.cache;

import kr.hhplus.be.server.infra.lock.RedisLock;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import kr.hhplus.be.server.support.utils.CacheKeys;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RList;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponIssueRepository {
	private final RedissonClient redissonClient;

	@RedisLock(lockKey = "'lock:coupon:' + #couponId")
	public RSet<Long> issueCoupon(Long userId,String couponId) {
		Object[] args = new Object[]{couponId,userId};
		RList<Long> requestUsers = redissonClient.getList(CacheKeys.COUPON_ISSUE_REQUEST_USER.getKey(args));
		RAtomicLong stock = redissonClient.getAtomicLong(CacheKeys.COUPON_STOCK.getKey(couponId));
		RSet<Long> userSet = redissonClient.getSet(CacheKeys.COUPON_ISSUED_USER.getKey(args));
		if (userSet.contains(userId)) {
			throw new CustomException(ErrorCode.DUPLICATE_COUPON_CLAIM);
		}

		requestUsers.add(userId);
		stock.decrementAndGet();
		if(stock.get() < 0){
			stock.incrementAndGet(); // 재고 수량 원복
			throw new CustomException(ErrorCode.COUPON_SOLD_OUT);
		}
		// 요청이 쿠폰 재고 수보다 많은 경우, 선착순으로 자르기
		int processSize = (int) Math.min(stock.get(), requestUsers.size());
		List<Long> validUsers = requestUsers.range(0, processSize - 1);

		userSet.addAll(validUsers);

		return userSet;
	}
	RAtomicLong initCouponStock(String couponId,int initialQuantity){
		RAtomicLong stock = redissonClient.getAtomicLong(CacheKeys.COUPON_STOCK.getKey(couponId));
		stock.set(initialQuantity);
		return stock;
	}
}
