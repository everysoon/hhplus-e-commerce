package kr.hhplus.be.server.infra.user.repository;

import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserCouponRepositoryImpl implements UserCouponRepository {

	@Override
	public List<UserCoupon> findByUserId(Long userId) {
		return List.of();
	}
}
