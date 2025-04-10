package kr.hhplus.be.server.infra.user.repository;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public class UserRepositoryImpl implements UserRepository {

	@Override
	public User findByUserId(Long userId) {
		return null;
	}

	@Override
	public User charge(Long userId, BigDecimal amount) {
		return null;
	}

	@Override
	public User use(Long userId, BigDecimal amount) {
		return null;
	}

	@Override
	public User save(User user) {
		return null;
	}
}
