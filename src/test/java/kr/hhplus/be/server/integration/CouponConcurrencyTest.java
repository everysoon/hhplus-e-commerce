package kr.hhplus.be.server.integration;

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
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class CouponConcurrencyTest extends BaseIntegrationTest {
	@Autowired
	private CouponService couponService;

	@Autowired
	private CouponRepository couponRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserCouponRepository userCouponRepository;

	private Coupon coupon;
	private final int threadCount = 10;
	private ExecutorService executor;
	private CountDownLatch latch;

	@BeforeAll
	void init() {
		coupon = CouponTestFixture.create(UUID.randomUUID().toString());
		IntStream.rangeClosed(1, threadCount).forEach(i -> {
			userRepository.save(UserTestFixture.createUser((long) i));
		});
		executor = Executors.newFixedThreadPool(threadCount);
		latch = new CountDownLatch(threadCount);
	}

	@Test
	void 선착순쿠폰_발급시_재고가1개_남았음에도_10명의_유저가_발급요청을_한다면_동시성제어가_되지않아_모두_발급된다() throws InterruptedException {
		Coupon issue = couponRepository.issue(coupon);
		List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

		List<User> users = userRepository.findAll();
		for (User user : users) {
			executor.submit(() -> {
				try {
					CouponCommand.Issue command = CouponCommand.Issue.of(user.getId(), issue.getId());
					couponService.issueCoupon(command);
				} catch (Exception e) {
					System.out.println("exception"+e.getMessage());
					exceptions.add(e);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		List<UserCoupon> issuedCoupons = userCouponRepository.findAll();
		int issuedCount = issuedCoupons.size();
		// 실패해야 하는 조건: 1장 이상 발급되면 동시성 문제가 발생한 것
		assertThat(issuedCount).isGreaterThan(1);
	}
}
