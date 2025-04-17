package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PointHistoryRepository pointHistoryRepository;

	public User getUserPoint(Long userId) {
		return userRepository.findByUserId(userId);
	}

	public User get(Long userId) {
		return userRepository.findByUserId(userId);
	}
}
