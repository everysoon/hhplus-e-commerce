package kr.hhplus.be.server.application.coupon;

import java.util.UUID;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CouponService implements CouponUseCase{
	private final CouponRepository couponRepository;
	public Coupon findById(UUID id){
		return  couponRepository.findById(id);
	}
}
