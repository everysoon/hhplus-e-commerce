package kr.hhplus.be.server.kafka;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.coupon.event.CouponIssuedEvent;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.infra.kafka.publisher.KafkaEventPublisher;
import kr.hhplus.be.server.utils.CouponTestFixture;
import kr.hhplus.be.server.utils.UserTestFixture;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = {"coupon.issued"})
public class CouponIssueKafkaIntegrationTest {
	@Autowired
	private KafkaEventPublisher kafkaEventPublisher;

	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserCouponRepository userCouponRepository;

	@Test
	void 쿠폰발급_이벤트가_소비되고_DB에_저장된다() throws InterruptedException {
		// 준비

		User user = UserTestFixture.createUser(1L);
		userRepository.save(user);
		Coupon  coupon = CouponTestFixture.create(UUID.randomUUID().toString());
		couponRepository.save(coupon);

		CouponIssuedEvent event = new CouponIssuedEvent(user.getId(), coupon.getId());

		// Kafka로 발행
		kafkaEventPublisher.publish("coupon.issued", event);

		Awaitility.await()
			.atMost(2, TimeUnit.SECONDS)
			.untilAsserted(() -> {
				Coupon savedCoupon = couponRepository.findById(coupon.getId());
				assertThat(savedCoupon).isNotNull();

				List<UserCoupon> coupons = userCouponRepository.findByUserIdAndCouponIds(
					user.getId(), List.of(savedCoupon.getId())
				);
				assertThat(coupons.size()).isGreaterThan(0);
			});
	}
}
