package kr.hhplus.be.server.infra.user.repository;

import java.util.List;
import kr.hhplus.be.server.config.jpa.InMemoryRepository;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.infra.user.entity.UserCouponEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserCouponRepositoryImpl extends InMemoryRepository<Long, UserCouponEntity> implements
	UserCouponRepository {

	@Override
	public List<UserCoupon> findByUserId(Long userId) {
		return List.of();
	}
}
