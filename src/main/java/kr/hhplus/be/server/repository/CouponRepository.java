package kr.hhplus.be.server.repository;

import kr.hhplus.be.server.entity.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository<Coupon, Long> {
}
