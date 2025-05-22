package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.application.coupon.CouponIssueDLQHandler;
import kr.hhplus.be.server.application.coupon.CouponIssuedEvent;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import kr.hhplus.be.server.utils.CouponTestFixture;
import kr.hhplus.be.server.utils.UserTestFixture;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CouponIssueStreamTest extends BaseIntegrationTest {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	@Autowired
	private CouponService couponService;
	@Autowired
	private UserCouponRepository couponRepository;
	@Autowired
	private UserCouponRepository userCouponRepository;
	@Autowired
	private CouponIssueDLQHandler dlqProcessor;

	@Autowired
	private UserRepository userRepository;

	private static final String COUPON_STREAM_KEY = "coupon:issue:stream";
	private static final String DLQ_STREAM_KEY = "coupon:issue:dlq";

	@Test
	void 쿠폰_발급_이벤트가_정상적으로_처리되어_DB에_저장된다(){
		// given
		Long userId = 1L;
		String couponId = UUID.randomUUID().toString();
		userRepository.save(UserTestFixture.createUser(userId));
		couponService.save(CouponTestFixture.create(couponId));
		// when
		couponService.issueCoupon(new CouponCommand.Issue(userId, couponId));
		// then
		List<UserCoupon> saved = userCouponRepository.findByUserIdAndCouponIds(userId,List.of(couponId));
		assertThat(saved.size()).isGreaterThan(0);
		assertThat(saved.get(0).getCouponId()).isEqualTo(couponId);
		assertThat(saved.get(0).getUserId()).isEqualTo(userId);
	}

	@Test
	void 쿠폰_이슈_리스너에서_예외가_발생하면_DLQ로_이동된다(){
		// given - 존재하지 않는 couponId (예외 유발)
		final String GROUP_NAME = "coupon-dlq-group";
		final String CONSUMER_NAME = "dlq-processor";
		Long userId = 1L;
		String couponId = UUID.randomUUID().toString(); // DB에서 찾을 수 없어 예외 발생
		userRepository.save(UserTestFixture.createUser(userId));
		// when
		redisTemplate.opsForStream().createGroup(DLQ_STREAM_KEY, GROUP_NAME);
		eventPublisher.publishEvent(new CouponIssuedEvent(userId, couponId));
		// then
		List<MapRecord<String, Object, Object>> dlqRecords = redisTemplate.opsForStream()
			.read(
				Consumer.from(GROUP_NAME, CONSUMER_NAME),
				StreamReadOptions.empty().count(10).block(Duration.ofSeconds(2)),
				StreamOffset.create(DLQ_STREAM_KEY, ReadOffset.lastConsumed())
			);
		assertThat(dlqRecords).isNotEmpty();

		Map<Object, Object> messageBody = dlqRecords.get(0).getValue();
		assertThat(messageBody.get("userId")).isEqualTo(userId.toString());
	}

	@Test
	void Redis기반_쿠폰발급_정확성과_중복방지_검증() throws Exception {
		// given
		int totalCoupons = 100;
		int totalUsers = 1000;
		int threads = 500;
		String couponId = UUID.randomUUID().toString();
		couponService.save(CouponTestFixture.create(couponId));

		redisTemplate.opsForValue().set("cache:coupon:stock:" + couponId, String.valueOf(totalCoupons));

		ExecutorService executor = Executors.newFixedThreadPool(threads);
		CountDownLatch latch = new CountDownLatch(totalUsers);

		for (int i = 0; i < totalUsers; i++) {
			Long userId = (long) i;
			userRepository.save(UserTestFixture.createUser(userId));
			executor.submit(() -> {
				try {
					couponService.issueCoupon(new CouponCommand.Issue(userId,couponId));
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(10, TimeUnit.SECONDS);
		executor.shutdown();

		// then
		List<UserCoupon> issuedCoupons = couponRepository.findByCouponIds(List.of(couponId));
		assertThat(issuedCoupons).hasSize(totalCoupons);
		Set<Long> distinctUsers = issuedCoupons.stream().map(UserCoupon::getUserId).collect(Collectors.toSet());
		assertThat(distinctUsers).hasSize(totalCoupons); // 중복 없음
	}
}
