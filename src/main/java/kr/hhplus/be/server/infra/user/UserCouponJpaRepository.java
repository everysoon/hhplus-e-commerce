package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.domain.coupon.UserCoupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCouponJpaRepository extends JpaRepository<UserCoupon, Long> {
	List<UserCoupon> findByUserId(Long userId);

	@Query("""
		    SELECT uc FROM UserCoupon uc
		    JOIN User  u ON uc.userId = u.id
		    JOIN Coupon  c ON c.id = uc.couponId
		    WHERE uc.userId = :userId AND uc.couponId IN :couponIds
		""")
	List<UserCoupon> findByUserIdAndCouponIds(@Param("userId") Long userId, @Param("couponIds") List<String> couponIds);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE UserCoupon uc SET uc.status = kr.hhplus.be.server.domain.coupon.CouponStatus.EXPIRED" +
		" WHERE uc.status = kr.hhplus.be.server.domain.coupon.CouponStatus.ISSUED AND uc.couponId IN :expiredCouponIds")
	List<UserCoupon> updateExpiredAll(List<String> expiredCouponIds);

	@Query("SELECT uc FROM UserCoupon uc WHERE uc.couponId IN :couponIds")
	List<UserCoupon> findByCouponIds(List<String> couponIds);
}
