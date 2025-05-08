package kr.hhplus.be.server.integration.concurrency;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import java.util.List;
import java.util.stream.IntStream;
import kr.hhplus.be.server.application.coupon.CouponCommand;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import kr.hhplus.be.server.utils.CouponTestFixture;
import kr.hhplus.be.server.utils.UserTestFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@Slf4j
public class CouponConcurrencyTest extends BaseIntegrationTest {

	@Autowired
	private CouponService couponService;
	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserCouponRepository userCouponRepository;

	@Autowired
	private ConcurrencyTestHelper concurrencyTestHelper;

	private final int threadCount = 10;

	@Test
	@DisplayName("단일 쿠폰의 재고가 부족할때 해당 쿠폰 재고만큼의 유저에게만 발급이 성공한다.")
	void 선착순쿠폰_발급시_쿠폰_재고가_1개_남았으면_10명중_1명만_발급에_성공한다() throws InterruptedException {
		IntStream.rangeClosed(1, threadCount).forEach(i -> {
			userRepository.save(UserTestFixture.createUser((long) i));
		});
		Coupon issue = couponRepository.save(CouponTestFixture.create(1));
		List<User> users = userRepository.findAll();
		concurrencyTestHelper.run(threadCount, index -> {
			User user = users.get(index);
			CouponCommand.Issue command = CouponCommand.Issue.of(user.getId(), issue.getId());
			couponService.issueCoupon(command);
		});

		List<UserCoupon> issuedCoupons = userCouponRepository.findAll();
		int issuedCount = issuedCoupons.size();

		assertThat(users.size()).isEqualTo(10);
		assertThat(issuedCount).isEqualTo(1);
		assertThat(issuedCoupons.get(0).getCoupon().getRemainingQuantity()).isEqualTo(0);
		assertThat(concurrencyTestHelper.getExceptions().size()).isEqualTo(9);
	}

	@Test
	void 여러유저가_여러_선착순쿠폰에_동시에_접근하면_각_쿠폰마다_최대1명만_발급된다() throws InterruptedException {
		// given: 3개의 재고 1인 쿠폰 저장 , 10명의 유저
		IntStream.rangeClosed(1, threadCount).forEach(i -> {
			userRepository.save(UserTestFixture.createUser((long) i));
		});
		List<Coupon> coupons = IntStream.range(0, 3)
			.mapToObj(i -> CouponTestFixture.create(1))
			.map(couponRepository::save)
			.toList();

		List<User> users = userRepository.findAll(); // 10명
		assertThat(users.size()).isEqualTo(10);

		// when: 10명의 유저가 각각 랜덤 쿠폰 1개에 대해 동시에 발급 시도
		concurrencyTestHelper.run(threadCount, index -> {
			User user = users.get(index);
			Coupon randomCoupon = coupons.get(index % 3); // 0,1,2 반복
			CouponCommand.Issue command = CouponCommand.Issue.of(user.getId(), randomCoupon.getId());
			couponService.issueCoupon(command);
		});

		// then: 총 발급된 쿠폰 수는 3개
		List<UserCoupon> issuedCoupons = userCouponRepository.findAll();
		assertThat(issuedCoupons.size()).isEqualTo(3);

		log.info("issuedCoupons: {}", issuedCoupons);

		// 각 쿠폰의 재고는 0
		coupons.forEach(coupon -> {
			Coupon refreshed = couponRepository.findById(coupon.getId());
			assertThat(refreshed.getRemainingQuantity()).isEqualTo(0);
		});
	}
}
