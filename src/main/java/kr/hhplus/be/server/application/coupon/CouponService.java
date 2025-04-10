package kr.hhplus.be.server.application.coupon;

import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CouponService {
	private final CouponRepository couponRepository;

	public Coupon findById(UUID id) {
		return couponRepository.findById(id).orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_COUPON));
	}
	public Coupon save(Coupon coupon) {
		return couponRepository.save(coupon);
	}
}
