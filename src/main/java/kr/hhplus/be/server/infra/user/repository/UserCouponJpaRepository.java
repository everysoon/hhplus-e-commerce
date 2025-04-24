package kr.hhplus.be.server.infra.user.repository;

import kr.hhplus.be.server.infra.coupon.entity.UserCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserCouponJpaRepository extends JpaRepository<UserCouponEntity, Long> {
	List<UserCouponEntity> findByUserId(Long userId);

	@Query("""
		    SELECT uc FROM UserCouponEntity uc
		    JOIN UserEntity  u ON uc.userId = u.id
		    JOIN CouponEntity  c ON c.id = uc.couponId
		    WHERE uc.userId = :userId AND uc.couponId IN :couponIds
		""")
	List<UserCouponEntity> findByUserIdAndCouponIds(@Param("userId") Long userId, @Param("couponIds") List<String> couponIds);

	@Modifying(clearAutomatically = true)
	@Query("UPDATE UserCouponEntity uc SET uc.status = kr.hhplus.be.server.domain.coupon.CouponStatus.EXPIRED" +
		" WHERE uc.status = kr.hhplus.be.server.domain.coupon.CouponStatus.ISSUED AND uc.couponId IN :expiredCouponIds")
	List<UserCouponEntity> updateExpiredAll(List<String> expiredCouponIds);

	@Query("SELECT uc FROM UserCouponEntity uc WHERE uc.couponId IN :couponIds")
	List<UserCouponEntity> findByCouponIds(List<String> couponIds);
}
