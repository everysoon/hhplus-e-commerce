package kr.hhplus.be.server.infra.user.repository;

import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponValidCommand;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import org.springframework.stereotype.Repository;

@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {

	@Override
	public List<UserCoupon> findByUserId(Long userId) {
		return List.of();
	}

	@Override
	public Boolean existsByUserIdAndCouponId(CouponValidCommand command) {
		return null;
	}

	@Override
	public long countCouponByUserId(IssueCouponCommand command) {
		return 0;
	}

	@Override
	public UserCoupon save(UserCoupon coupon) {
		return null;
	}

}
