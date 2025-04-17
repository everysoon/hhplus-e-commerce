package kr.hhplus.be.server.infra.user.repository;

import java.util.List;
import kr.hhplus.be.server.infra.coupon.entity.UserCouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserCouponJpaRepository extends JpaRepository<UserCouponEntity, Long> {
	List<UserCouponEntity> findByUserId(Long userId);
	@Query("""
        SELECT uc FROM UserCouponEntity uc
        JOIN UserEntity  u ON uc.userId = u.id
        JOIN CouponEntity  c ON c.id = uc.couponId
        WHERE uc.userId = :userId AND uc.couponId IN :couponIds
    """)
	List<UserCouponEntity> findByUserIdAndCouponIds(@Param("userId") Long userId, @Param("couponIds") List<String> couponIds);
}
