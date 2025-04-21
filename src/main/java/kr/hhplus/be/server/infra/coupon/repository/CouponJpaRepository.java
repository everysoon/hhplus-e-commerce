package kr.hhplus.be.server.infra.coupon.repository;

import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, String> {
	List<CouponEntity> findAllByIdIn(List<String> ids);
}
