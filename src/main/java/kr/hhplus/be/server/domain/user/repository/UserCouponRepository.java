package kr.hhplus.be.server.domain.user.repository;

import kr.hhplus.be.server.domain.user.UserCoupon;

import java.util.List;
import java.util.UUID;

public interface UserCouponRepository {
	List<UserCoupon> findByUserId(Long userId);
	/**@Query("""
    SELECT COUNT(uc)
    FROM UserCoupon uc
    WHERE uc.user.id = :userId
    AND uc.coupon.uuid IN :couponUuids
	""")**/
	Boolean existsByUserIdAndCouponId(Long userId, List<UUID> couponIds);
}
