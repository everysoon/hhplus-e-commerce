package kr.hhplus.be.server.infra.cache;

import kr.hhplus.be.server.support.aop.lock.RedisLock;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import kr.hhplus.be.server.support.utils.CacheKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RAtomicLong;
import org.redisson.api.RList;
import org.redisson.api.RSet;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CouponIssueRepository {
	private final RedissonClient redissonClient;

	@RedisLock(lockKey = "'lock:coupon:' + #couponId")
	public RSet<Long> issueCoupon(Long userId,String couponId) {
		log.info("### CouponIssueRepository : userId = {}, couponId = {}", userId, couponId);
		Object[] args = new Object[]{couponId,userId};
		RList<Long> requestUsers = redissonClient.getList(CacheKeys.COUPON_ISSUE_REQUEST_USER.getKey(args));
		RAtomicLong stock = redissonClient.getAtomicLong(CacheKeys.COUPON_STOCK.getKey(couponId));
		RSet<Long> userSet = redissonClient.getSet(CacheKeys.COUPON_ISSUED_USER.getKey(args));
		if (userSet.contains(userId)) {
			throw new CustomException(ErrorCode.DUPLICATE_COUPON_CLAIM);
		}
		log.info("### coupon : stock = {}", stock);

		stock.decrementAndGet();
		if(stock.get() < 0){
			stock.incrementAndGet(); // 재고 수량 원복
			throw new CustomException(ErrorCode.COUPON_SOLD_OUT);
		}

		requestUsers.add(userId);
		// 요청이 쿠폰 재고 수보다 많은 경우, 선착순으로 자르기
		int processSize = (int) Math.min(stock.get(), requestUsers.size());
		List<Long> validUsers = requestUsers.range(0, processSize - 1);
		boolean isContains = validUsers.contains(userId);
		if(isContains){
			userSet.add(userId);
		}
		log.info("### userSet  = {}", userSet);
		return userSet;
	}
	RAtomicLong initCouponStock(String couponId,int initialQuantity){
		RAtomicLong stock = redissonClient.getAtomicLong(CacheKeys.COUPON_STOCK.getKey(couponId));
		stock.set(initialQuantity);
		return stock;
	}
}
