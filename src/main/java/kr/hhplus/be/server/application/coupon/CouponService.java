package kr.hhplus.be.server.application.coupon;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponValidator;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.infra.NotificationSender;
import kr.hhplus.be.server.infra.lock.RedisLock;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final Logger logger = LoggerFactory.getLogger(CouponService.class);
	private final CouponRepository couponRepository;
	private final UserCouponRepository userCouponRepository;
	private final CouponValidator couponValidator;
	private final NotificationSender notificationSender;

	@Transactional(readOnly = true)
	public List<UserCouponDetailResult> getUserCoupons(Long userId) {
		return userCouponRepository.findByUserId(userId).stream().map(UserCouponDetailResult::of)
			.toList();
	}

	@Transactional(readOnly = true)
	public List<Coupon> findCouponByUserId(Long userId) {
		logger.info("### findCouponByUserId parameter : {}", userId);
		List<String> couponIds = userCouponRepository.findByUserId(userId)
			.stream()
			.map(UserCoupon::getCoupon)
			.map(Coupon::getId)
			.toList();
		return couponRepository.findAllByIdIn(couponIds);
	}

	@Transactional(propagation = MANDATORY)
	public void restore(CouponCommand.Restore command) {
		logger.info("### restore parameter : {}", command.toString());
		if (command.coupons() == null || command.coupons().isEmpty()) {
			return;
		}
		List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndCouponIds(
				command.userId(), command.couponIds())
			.stream()
			.map(UserCoupon::isValidRestore)
			.toList();
		List<Coupon> coupons = userCoupons.stream().map(UserCoupon::getCoupon).toList();
		userCoupons.forEach(UserCoupon::restore);
		coupons.forEach(Coupon::increaseStock);
		userCouponRepository.updateAll(userCoupons);
		couponRepository.updateAll(coupons);
	}

	@Transactional
	@RedisLock(lockKey = "user:{#command.userId()}:coupon:{#command.couponId()}", params = "#command.userId(), #command.couponId()")
	public UserCouponDetailResult issueCoupon(CouponCommand.Issue command) {
		logger.info("### issueCoupon parameter : {}", command.toString());
		couponValidator.isCouponIdValidUuid(command.couponId());
		couponValidator.duplicateIssued(command.userId(), command.couponId());
		Coupon coupon = couponRepository.issue(command.couponId());
		couponValidator.isValidCoupon(coupon);
		UserCoupon userCoupon = userCouponRepository.save(command.toUnitCouponValid(coupon));
		return UserCouponDetailResult.of(userCoupon);
	}

	@Transactional(propagation = MANDATORY)
	public UseCouponInfo use(CouponCommand.Use command) {
		if (command.couponIds() == null || command.couponIds().isEmpty()) {
			return new UseCouponInfo(command.userId(), null);
		}
		logger.info("### use parameter : {}", command);
		List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndCouponIds(
				command.userId(), command.couponIds())
			.stream()
			.map(UserCoupon::use)
			.toList();
		userCouponRepository.saveAll(userCoupons);
		userCoupons.stream()
			.map(UserCoupon::getCoupon)
			.forEach(Coupon::validExpired);
		return new UseCouponInfo(
			command.userId(),
			userCoupons.stream().map(UserCoupon::getCoupon).toList()
		);
	}

	public Coupon findCouponById(String id) {
		logger.info("### findCouponById parameter : {}", id);
		couponValidator.isCouponIdValidUuid(id);
		return couponRepository.findById(id);
	}

	@Transactional
	public void scheduleExpireCoupons() {
		LocalDateTime now = LocalDateTime.now();
		List<String> expiredCouponIds = couponRepository.findExpiredAll(now).stream()
			.map(Coupon::getId).toList();
		logger.info("### scheduleExpireCoupons {}", expiredCouponIds);
		if (!expiredCouponIds.isEmpty()) {
			userCouponRepository.updateExpiredCoupons(expiredCouponIds);
		}
	}

	@Transactional
	public void scheduleNotifyUserBeforeExpireDay() {
		LocalDateTime expiredAt = LocalDateTime.now().minusDays(1);
		List<String> expiredCouponIds = couponRepository.findExpiredAll(expiredAt).stream()
			.map(Coupon::getId).toList();
		List<UserCoupon> userCoupons = userCouponRepository.findByCouponIds(expiredCouponIds);
		logger.info("### scheduleNotifyUserBeforeExpireDay {}", userCoupons.size());
		userCoupons.forEach(uc -> {
			logger.info("### TEMP SEND Notification");
			String message =
				uc.getUserId() + "님, " + uc.getCoupon().getDescription() + " 쿠폰이 만료 하루 전입니다.";
			notificationSender.sendNotification(message);
		});
	}
}
