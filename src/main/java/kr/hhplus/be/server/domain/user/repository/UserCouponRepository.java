package kr.hhplus.be.server.domain.user.repository;

import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponValidCommand;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.domain.user.UserCoupon;

public interface UserCouponRepository {
	List<UserCoupon> findByUserId(Long userId);
	/**@Query("""
    SELECT COUNT(uc)
    FROM UserCoupon uc
    WHERE uc.user.id = :userId
    AND uc.coupon.uuid IN :couponUuids
	""")**/
	Boolean existsByUserIdAndCouponId(CouponValidCommand command);
	long countCouponByUserId(IssueCouponCommand command);
	UserCoupon save(UserCoupon coupon);
}
