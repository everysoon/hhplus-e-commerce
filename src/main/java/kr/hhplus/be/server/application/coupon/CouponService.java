package kr.hhplus.be.server.application.coupon;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponValidator;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
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

	@Transactional(readOnly = true)
	public List<UserCouponDetailResult> getUserCoupons(Long userId) {
		return userCouponRepository.findByUserId(userId).stream().map(UserCouponDetailResult::of).toList();
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
		if(command.coupons() == null || command.coupons().isEmpty()){
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
		if (command.couponIds().isEmpty()) {
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
}
