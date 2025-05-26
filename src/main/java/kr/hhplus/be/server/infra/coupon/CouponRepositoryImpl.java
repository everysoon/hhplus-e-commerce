package kr.hhplus.be.server.infra.coupon;

import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CouponRepositoryImpl implements CouponRepository {
	private final CouponJpaRepository couponJpaRepository;

	@Override
	public Coupon findById(String id) {
		return couponJpaRepository.findById(id)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_COUPON));
	}

	@Override
	public Coupon issue(String id) {
		Coupon coupon = couponJpaRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_COUPON));
		return coupon.issue(1);
	}

	@Override
	public Coupon issue(String id, int quantity) {
		Coupon coupon = couponJpaRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_COUPON));
		return coupon.issue(quantity);
	}

	@Override
	public void updateAll(List<Coupon> coupons) {
		couponJpaRepository.saveAll(coupons);
	}

	@Override
	public List<Coupon> findAll() {
		return couponJpaRepository.findAll();
	}

	@Override
	public List<Coupon> findExpiredAll(LocalDateTime expiredAt) {
		return couponJpaRepository.findExpiredAll(expiredAt);
	}

	@Override
	public List<Coupon> findNotExpiredAll(LocalDateTime expiredAt) {
		return couponJpaRepository.findNotExpiredAll(expiredAt);
	}

	@Override
	public Coupon save(Coupon coupon) {
		return couponJpaRepository.save(coupon);
	}

	@Override
	public List<Coupon> findAllByIdIn(List<String> couponIds) {
		return couponJpaRepository.findAllByIdIn(couponIds);
	}
}
