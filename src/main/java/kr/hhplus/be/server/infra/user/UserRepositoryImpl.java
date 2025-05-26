package kr.hhplus.be.server.infra.user;

import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

	private final UserJpaRepository userJpaRepository;

	@Override
	public User findById(Long userId) {
		return userJpaRepository.findById(userId)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_USER));
	}

	@Override
	public User save(User user) {
		return userJpaRepository.save(user);
	}

	@Override
	public List<User> findAll() {
		return userJpaRepository.findAll().stream().toList();
	}
}
