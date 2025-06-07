package kr.hhplus.be.server.infra.cache;

import kr.hhplus.be.server.domain.coupon.event.CouponIssuedEvent;
import kr.hhplus.be.server.infra.kafka.publisher.KafkaEventPublisher;
import kr.hhplus.be.server.support.aop.lock.RedisLock;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import kr.hhplus.be.server.support.utils.CacheKeys;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.*;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CouponIssueRepository {
	private final RedissonClient redissonClient;
	private final KafkaEventPublisher kafkaEventPublisher;
	private static final String LUA_SCRIPT = """
		    local stock = tonumber(redis.call("GET", KEYS[1]))
		    if not stock then
		        return -3
		    end
		    if stock <= 0 then
		        return -1
		    end
			local userIdStr = ARGV[1]
			userIdStr = string.gsub(userIdStr, '^"(.*)"$', '%1')
			redis.call("PUBLISH", "debug", userIdStr)
		    if redis.call("SISMEMBER", KEYS[2], ARGV[1]) == 1 then
		        return 0
		    end

		    redis.call("DECR", KEYS[1])
		    redis.call("SADD", KEYS[2], userIdStr)
		    return 1
		""";

	public boolean issueCouponV2(Long userId, String couponId) {
		String stockKey = CacheKeys.COUPON_STOCK.getKey(couponId);
		String userSetKey = String.format("cache:coupon:%s:users:issued",couponId);

		Long result = redissonClient.getScript().eval(
			RScript.Mode.READ_WRITE,
			LUA_SCRIPT,
			RScript.ReturnType.INTEGER,
			Arrays.asList(stockKey, userSetKey),
			userId.toString()
		);
		if (result == -1) throw new CustomException(ErrorCode.COUPON_SOLD_OUT);
		if (result == 0) throw new CustomException(ErrorCode.DUPLICATE_COUPON_CLAIM);

		// 발급 성공 시 Kafka 이벤트 발행
		kafkaEventPublisher.publish("coupon.issued", new CouponIssuedEvent(userId, couponId));
		return true;
	}

	@RedisLock(lockKey = "'lock:coupon:' + #couponId")
	public RSet<Long> issueCoupon(Long userId, String couponId) {
		log.info("### CouponIssueRepository : userId = {}, couponId = {}", userId, couponId);
		Object[] args = new Object[]{couponId, userId};
		RList<Long> requestUsers = redissonClient.getList(CacheKeys.COUPON_ISSUE_REQUEST_USER.getKey(args));
		RAtomicLong stock = redissonClient.getAtomicLong(CacheKeys.COUPON_STOCK.getKey(couponId));
		RSet<Long> userSet = redissonClient.getSet(CacheKeys.COUPON_ISSUED_USER.getKey(args));
		if (userSet.contains(userId)) {
			throw new CustomException(ErrorCode.DUPLICATE_COUPON_CLAIM);
		}
		log.info("### coupon : stock = {}", stock);

		stock.decrementAndGet();
		if (stock.get() < 0) {
			stock.incrementAndGet(); // 재고 수량 원복
			throw new CustomException(ErrorCode.COUPON_SOLD_OUT);
		}
		kafkaEventPublisher.publish("coupon.issued", new CouponIssuedEvent(userId, couponId));
		requestUsers.add(userId);

		// 요청이 쿠폰 재고 수보다 많은 경우, 선착순으로 자르기
		int processSize = (int) Math.min(stock.get(), requestUsers.size());
		List<Long> validUsers = requestUsers.range(0, processSize - 1);
		boolean isContains = validUsers.contains(userId);
		if (isContains) {
			userSet.add(userId);
		}
		log.info("### userSet  = {}", userSet);
		return userSet;
	}

	RAtomicLong initCouponStock(String couponId, int initialQuantity) {
		RAtomicLong stock = redissonClient.getAtomicLong(CacheKeys.COUPON_STOCK.getKey(couponId));
		stock.set(initialQuantity);
		return stock;
	}
}
