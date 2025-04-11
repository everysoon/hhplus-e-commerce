package kr.hhplus.be.server.application.user;

import kr.hhplus.be.server.domain.user.PointHistory;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.infra.user.entity.PointStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PointHistoryRepository pointHistoryRepository;

	public User getUserPoint(Long userId) {
		return userRepository.findByUserId(userId);
	}

	public User chargePoint(Long userId, BigDecimal price) {
		User user = userRepository.findByUserId(userId);
		user.charge(price);
		PointHistory history = PointHistory.from(userId, price, PointStatus.CHARGED);
		pointHistoryRepository.save(history);
		return userRepository.save(user);
	}

	public User usePoint(Long userId, BigDecimal price) {
		User user = userRepository.findByUserId(userId);
		user.use(price);
		PointHistory history = PointHistory.from(userId, price, PointStatus.USED);
		pointHistoryRepository.save(history);
		return userRepository.save(user);
	}

	public User get(Long userId) {
		return userRepository.findByUserId(userId);
	}
}
