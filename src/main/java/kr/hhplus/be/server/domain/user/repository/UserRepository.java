package kr.hhplus.be.server.domain.user.repository;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.user.User;

public interface UserRepository {
	User findByUserId(Long userId);
	User charge(Long userId, BigDecimal amount);
	User use(Long userId, BigDecimal amount);
	User save(User user);
}
