package kr.hhplus.be.server.application.coupon;

import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService {
	private final Logger logger = LoggerFactory.getLogger(CouponService.class);
	private final CouponRepository couponRepository;
	private final UserCouponRepository userCouponRepository;
	public List<Coupon> findAll(){
		return couponRepository.findAll();
	}
	public List<UserCoupon> findUserCouponByUserId(Long userId) {
		logger.info("### findUserCouponByUserId parameter : {}", userId);
		return userCouponRepository.findByUserId(userId);
	}
	public void restore(UseCouponCommand command){
		logger.info("### restore parameter : {}",command.toString());
		List<String> couponIds = command.coupons().stream().map(Coupon::getId).toList();
		List<UserCoupon> userCoupons = userCouponRepository.findByUserIdAndCouponIds(command.user().getId(), couponIds);
		userCoupons.forEach(UserCoupon::restore);
		command.coupons().forEach(Coupon::increaseStock);
	}
	public List<Coupon> validateUserCoupons(CouponValidCommand command) {
		logger.info("### validateUserCoupons parameter : {}",command.toString());
		userCouponRepository.validateUserCoupons(command);
		return couponRepository.validateCoupons(command.couponIds());
	}
	public void validateDuplicateIssued(IssueCouponCommand command) {
		logger.info("### validateDuplicateIssued parameter : {}", command.toString());
		userCouponRepository.validateDuplicateIssued(command);
	}

	public List<UserCoupon> use(UseCouponCommand command) {
		logger.info("### use parameter : {}",command.toString());
		return command.getUserCoupons()
			.stream()
			.map(UserCoupon::use)
			.map(userCouponRepository::save)
			.toList();
	}

	public UserCoupon issueByUser(UserCoupon coupon) {
		logger.info("### issueByUser parameter : {}",coupon.toString());
		return userCouponRepository.save(coupon);
	}

	public Coupon findCouponById(String id) {
		logger.info("### findCouponById parameter : {}", id);
		return couponRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_COUPON));
	}
}
