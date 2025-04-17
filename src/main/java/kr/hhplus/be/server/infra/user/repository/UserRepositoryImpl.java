package kr.hhplus.be.server.infra.user.repository;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.infra.user.entity.UserEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
	private final UserJpaRepository userJpaRepository;
	@Override
	public User findById(Long userId) {
		return userJpaRepository.findById(userId)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_USER))
			.toDomain();
	}

	@Override
	public User save(User user) {
		return userJpaRepository.save(UserEntity.from(user)).toDomain();
	}
}
