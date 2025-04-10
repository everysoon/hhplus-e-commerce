package kr.hhplus.be.server.domain.user.repository;

import java.util.List;
import kr.hhplus.be.server.domain.user.UserCoupon;

public interface UserCouponRepository {
	List<UserCoupon> findByUserId(Long userId);
}
