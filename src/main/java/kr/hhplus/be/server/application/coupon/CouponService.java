package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponValidator;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.UserCouponRepository;
import kr.hhplus.be.server.infra.NotificationSender;
import kr.hhplus.be.server.infra.cache.CouponIssueService;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final Logger logger = LoggerFactory.getLogger(CouponService.class);
	private final CouponRepository couponRepository;
	private final UserCouponRepository userCouponRepository;
	private final CouponValidator couponValidator;
	private final NotificationSender notificationSender;
	private final CouponIssueService couponIssueService;

	private final ApplicationEventPublisher applicationEventPublisher;

	@Transactional(readOnly = true)
	public List<UserCouponDetailResult> getUserCoupons(Long userId) {
		return userCouponRepository.findByUserId(userId)
			.stream()
			.map(uc->{
				Coupon coupon = couponRepository.findById(uc.getCouponId());
				return UserCouponDetailResult.of(uc,coupon);
			})
			.toList();
	}

	@Transactional(readOnly = true)
	public List<Coupon> findCouponByUserId(Long userId) {
		logger.info("### findCouponByUserId parameter : {}", userId);
		List<String> couponIds = userCouponRepository.findByUserId(userId)
			.stream()
			.map(UserCoupon::getCouponId)
			.toList();
		return couponRepository.findAllByIdIn(couponIds);
	}

	@Transactional
	public void restore(CouponCommand.Restore command) {
		logger.info("### restore parameter : {}", command.toString());
		if (command.couponIds() == null || command.couponIds().isEmpty()) {
			return;
		}
		List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndCouponIds(
				command.userId(), command.couponIds())
			.stream()
			.map(UserCoupon::isValidRestore)
			.toList();
		List<Coupon> coupons = userCoupons.stream().map(UserCoupon::getCouponId).map(couponRepository::findById).toList();
		userCoupons.forEach(UserCoupon::restore);
		coupons.forEach(Coupon::increaseStock);
//		userCouponRepository.updateAll(userCoupons);
//		couponRepository.updateAll(coupons);
	}

	@Transactional
	public boolean issueCoupon(CouponCommand.Issue command) {
		logger.info("### issueCoupon parameter : {}", command.toString());
		RSet<Long> ids = couponIssueService.issueCoupon(command.userId(), command.couponId());
		applicationEventPublisher.publishEvent(command.toEvent());
		return ids.contains(command.userId());
	}
	public Coupon findById(String id) {
		return couponRepository.findById(id);
	}
	public Coupon save(Coupon coupon) {
		couponIssueService.initCouponStock(coupon.getId(), coupon.getInitialQuantity());
		return couponRepository.save(coupon);
	}

	@Transactional
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

		List<Coupon> coupons = userCoupons.stream()
			.map(UserCoupon::getCouponId)
			.map(couponRepository::findById).toList();

		coupons.forEach(Coupon::validExpired);
		return new UseCouponInfo(
			command.userId(),
			coupons
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
			Coupon coupon = couponRepository.findById(uc.getCouponId());
			logger.info("### TEMP SEND Notification");
			String message =
				uc.getUserId() + "님, " + coupon.getDescription() + " 쿠폰이 만료 하루 전입니다.";
			notificationSender.sendNotification(message);
		});
	}
}
