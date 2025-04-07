package kr.hhplus.be.server.infra.coupon.repository;

import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, Long> {
}
