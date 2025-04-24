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
import org.junit.jupiter.api.BeforeAll;
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

	private Coupon coupon;
	private final int threadCount = 10;

	@BeforeAll
	void init() {
		coupon = CouponTestFixture.create(1);
		IntStream.rangeClosed(1, threadCount).forEach(i -> {
			userRepository.save(UserTestFixture.createUser((long) i));
		});
	}


	@Test
	void 선착순쿠폰_발급시_쿠폰_재고가_1개_남았으면_10명중_1명만_발급에_성공한다() throws InterruptedException {
		Coupon issue = couponRepository.save(coupon);

		List<User> users = userRepository.findAll();
		concurrencyTestHelper.run(threadCount, index -> {
			User user = users.get(index);
			CouponCommand.Issue command = CouponCommand.Issue.of(user.getId(), issue.getId());
			couponService.issueCoupon(command);
		});

		List<UserCoupon> issuedCoupons = userCouponRepository.findAll();
		int issuedCount = issuedCoupons.size();

		assertThat(issuedCount).isEqualTo(1);
		assertThat(issuedCoupons.get(0).getCoupon().getRemainingQuantity()).isEqualTo(0);
		assertThat(concurrencyTestHelper.getExceptions().size()).isEqualTo(9);
	}
}
