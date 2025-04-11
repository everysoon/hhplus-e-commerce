package kr.hhplus.be.server.infra.coupon.repository;

import java.util.Optional;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public class CouponRepositoryImpl implements CouponRepository {
	@Override
	public Optional<Coupon> findById(UUID id) {
		return null;
	}

	@Override
	public Coupon save(Coupon coupon) {
		return null;
	}

}
