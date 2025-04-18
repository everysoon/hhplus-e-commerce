package kr.hhplus.be.server.application.coupon;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponValidator;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CouponService {

	private final Logger logger = LoggerFactory.getLogger(CouponService.class);
	private final CouponRepository couponRepository;
	private final UserCouponRepository userCouponRepository;
	private final CouponValidator couponValidator;

	public List<UserCouponDetailResult> getUserCoupons(Long userId) {
		return findUserCouponByUserId(userId).stream().map(uc -> {
			Coupon coupon = findCouponById(uc.getCouponId());
			return UserCouponDetailResult.of(uc, coupon);
		}).toList();
	}

	public List<UserCoupon> findUserCouponByUserId(Long userId) {
		logger.info("### findUserCouponByUserId parameter : {}", userId);
		return userCouponRepository.findByUserId(userId);
	}

	@Transactional
	public void restore(UseCouponCommand command) {
		logger.info("### restore parameter : {}", command.toString());

		List<Coupon> coupons = command.couponIds().stream()
			.map(this::findCouponById)
			.toList();

		List<String> couponIds = coupons.stream()
			.map(Coupon::getId).toList();

		List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndCouponIds(
			command.userId(), couponIds)
			.stream().map(UserCoupon::isValidRestore)
			.toList();

		userCoupons.forEach(UserCoupon::restore);
		coupons.forEach(Coupon::increaseStock);
		userCouponRepository.updateAll(userCoupons);
		couponRepository.updateAll(coupons);
	}
	public UserCouponDetailResult issueCoupon(IssueCouponCommand command) {
		// 쿠폰 중복 발급인지 확인
		couponValidator.isCouponIdValidUuid(command.couponId());
		Coupon coupon = findCouponById(command.couponId());
		coupon.issue();

		UserCoupon userCoupon = issueByUser(command);

		return UserCouponDetailResult.of(userCoupon, coupon);
	}
	public List<Coupon> validateUserCoupons(CouponValidCommand command) {
		logger.info("### validateUserCoupons parameter : {}", command.toString());
		command.couponIds().forEach(couponValidator::isCouponIdValidUuid);
		userCouponRepository.validateUserCoupons(command);
		return couponRepository.validateCoupons(command.couponIds());
	}

	public void validateDuplicateIssued(IssueCouponCommand command) {
		logger.info("### validateDuplicateIssued parameter : {}", command.toString());
		couponValidator.isCouponIdValidUuid(command.couponId());
		userCouponRepository.validateDuplicateIssued(command);
	}
	public UseCouponInfo use(UseCouponCommand command) {
		if (command.couponIds().isEmpty()) {
			return new UseCouponInfo(command.userId(),null);
		}
		logger.info("### use parameter : {}", command);
		List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndCouponIds(
				command.userId(), command.couponIds())
			.stream().map(UserCoupon::use)
			.toList();
		userCouponRepository.saveAll(userCoupons);
		List<Coupon> coupons = validateUserCoupons(
			CouponValidCommand.of(command.userId(), command.couponIds()));

		return new UseCouponInfo(
			command.userId(),
			coupons
		);
	}

	public UserCoupon issueByUser(IssueCouponCommand command) {
		logger.info("### issueByUser parameter : {}", command.toString());
		validateDuplicateIssued(command);
		return userCouponRepository.save(command);
	}

	public Coupon findCouponById(String id) {
		logger.info("### findCouponById parameter : {}", id);
		couponValidator.isCouponIdValidUuid(id);
		return couponRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_COUPON));
	}
}
