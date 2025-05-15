package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.coupon.CouponIssueDLQProcessor;
import kr.hhplus.be.server.application.coupon.CouponIssueEventPublisher;
import kr.hhplus.be.server.application.coupon.CouponIssuedEvent;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import kr.hhplus.be.server.utils.CouponTestFixture;
import kr.hhplus.be.server.utils.UserTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.StreamOffset;
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
import static org.testcontainers.shaded.org.awaitility.Awaitility.await;

public class CouponIssueStreamTest extends BaseIntegrationTest {

	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	@Autowired
	private CouponIssueEventPublisher eventPublisher;
	@Autowired
	private CouponService couponService;
	@Autowired
	private UserCouponRepository couponRepository;
	@Autowired
	private UserCouponRepository userCouponRepository;
	@Autowired
	private CouponIssueDLQProcessor dlqProcessor;

	@Autowired
	private UserRepository userRepository;

	private static final String COUPON_STREAM_KEY = "coupon:issue:stream";
	private static final String DLQ_STREAM_KEY = "coupon:issue:dlq";

	@BeforeEach
	void setup() {
		redisTemplate.delete(COUPON_STREAM_KEY);
		redisTemplate.delete(DLQ_STREAM_KEY);
	}

	@Test
	void 쿠폰_발급_이벤트가_정상적으로_처리되어_DB에_저장된다(){
		// given
		Long userId = 1L;
		String couponId = UUID.randomUUID().toString();
		userRepository.save(UserTestFixture.createUser(userId));
		couponService.save(CouponTestFixture.create(couponId));
		// when
		eventPublisher.publish(new CouponIssuedEvent(userId, couponId));

		// then
		await()
			.atMost(Duration.ofSeconds(5))
			.untilAsserted(() -> {
				List<UserCoupon> saved = userCouponRepository.findByUserIdAndCouponIds(userId, List.of(couponId));
				assertThat(saved.size()).isGreaterThan(0);
				assertThat(saved.get(0).getCoupon().getId()).isEqualTo(couponId);
				assertThat(saved.get(0).getUserId()).isEqualTo(userId);
			});
	}

	@Test
	void 쿠폰_이슈_리스너에서_예외가_발생하면_DLQ로_이동된다(){
		// given - 존재하지 않는 couponId (예외 유발)
		Long userId = 1L;
		String couponId = UUID.randomUUID().toString(); // DB에서 찾을 수 없어 예외 발생
		userRepository.save(UserTestFixture.createUser(userId));
		// when
		eventPublisher.publish(new CouponIssuedEvent(userId, couponId));
		// then
		await()
			.atMost(Duration.ofSeconds(5))
			.untilAsserted(() -> {
				List<MapRecord<String, Object, Object>> dlqRecords =
					redisTemplate.opsForStream().read(StreamOffset.fromStart(DLQ_STREAM_KEY));
				assertThat(dlqRecords).isNotEmpty();

				Map<Object, Object> messageBody = dlqRecords.get(0).getValue();
				assertThat(messageBody.get("userId")).isEqualTo(userId);
			});
	}

	@Test
	void 발급성공_이벤트가_Stream에_정상발행된다() {
		// given
		String userId = "user-001";
		Long couponId = 1L;
		CouponIssuedEvent event = new CouponIssuedEvent(couponId, userId);

		// when
		eventPublisher.publish(event);

		// then
		await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
			List<MapRecord<String, Object, Object>> records =
				redisTemplate.opsForStream().read(StreamOffset.fromStart(COUPON_STREAM_KEY));
			assertThat(records).isNotEmpty();
			Map<Object, Object> value = records.get(0).getValue();
			assertThat(value.get("couponId")).isEqualTo(String.valueOf(couponId));
			assertThat(value.get("userId")).isEqualTo(userId);
		});
	}

	@Test
	void StreamListener가_정상적으로_DB에_발급정보를_반영한다() {
		// given
		Long userId = 1L;
		String couponId = UUID.randomUUID().toString();
		userRepository.save(UserTestFixture.createUser(userId));
		couponService.save(CouponTestFixture.create(couponId));
		CouponIssuedEvent event = new CouponIssuedEvent(userId, couponId);

		// when
		eventPublisher.publish(event);

		// then
		await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
			List<UserCoupon> result = userCouponRepository.findByUserIdAndCouponIds(userId, List.of(couponId));
			assertThat(result.size()).isGreaterThan(0);
		});
	}

	@Test
	void 실패한_이벤트는_DLQ로_이동되고_DLQProcessor에서_재처리된다() {
		// given
		Long userId = 3L;
		String invalidCouponId = UUID.randomUUID().toString(); // 존재하지 않는 쿠폰 ID로 오류 유도
		userRepository.save(UserTestFixture.createUser(userId));
		CouponIssuedEvent event = new CouponIssuedEvent(userId, invalidCouponId);

		eventPublisher.publish(event);
		// then
		await().atMost(Duration.ofSeconds(5)).untilAsserted(() -> {
			List<MapRecord<String, Object, Object>> dlqRecords =
				redisTemplate.opsForStream().read(StreamOffset.fromStart(DLQ_STREAM_KEY));
			assertThat(dlqRecords).isNotEmpty();
		});

		// when
		dlqProcessor.processDLQMessages();

		// then
		List<UserCoupon> result = userCouponRepository.findByUserIdAndCouponIds(userId, List.of(invalidCouponId));
		assertThat(result.size()).isEqualTo(0); // 여전히 저장되지 않아야 정상
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
					eventPublisher.publish(new CouponIssuedEvent(userId, couponId));
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await(10, TimeUnit.SECONDS);
		executor.shutdown();

		// then
		await().atMost(Duration.ofSeconds(10)).untilAsserted(() -> {
			List<UserCoupon> issuedCoupons = couponRepository.findByCouponIds(List.of(couponId));
			assertThat(issuedCoupons).hasSize(totalCoupons);
			Set<Long> distinctUsers = issuedCoupons.stream().map(UserCoupon::getUserId).collect(Collectors.toSet());
			assertThat(distinctUsers).hasSize(totalCoupons); // 중복 없음
		});
	}
}
