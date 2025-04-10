package kr.hhplus.be.server.infra.user.repository;

import java.math.BigDecimal;
import kr.hhplus.be.server.config.jpa.InMemoryRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.infra.user.entity.UserEntity;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends InMemoryRepository<Long, UserEntity> implements
	UserRepository {

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
