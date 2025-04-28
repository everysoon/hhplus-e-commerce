package kr.hhplus.be.server.application.point;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.infra.lock.RedisLock;
import kr.hhplus.be.server.infra.point.entity.PointStatus;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;
	private final PointHistoryRepository pointHistoryRepository;

	@Transactional(propagation = MANDATORY)
	public Point refund(PointCommand.Refund command) {
		PointHistory pointHistory = new PointHistory(null, command.userId(), PointStatus.REFUND,
			command.totalPrice(), LocalDateTime.now());
		Point point = pointRepository.findByUserId(command.userId()).get();
		point.charge(command.totalPrice());
		Point save = pointRepository.save(point);
		pointHistoryRepository.save(pointHistory);
		return save;
	}

	@Transactional
	@RedisLock(lockKey = "user:point{#command.userId()}", params = "#command.userId()")
	public PointCommand.Detail charge(PointCommand.Charge command) {
		Point point = pointRepository.charge(command.userId(), command.amount());
		PointHistory history = PointHistory.from(command);
		pointHistoryRepository.save(history);
		return PointCommand.Detail.of(history, point.getBalance());
	}

	@Transactional(readOnly = true)
	public Point getUserPoint(Long userId) {
		return pointRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_POINT_BY_USER_ID));
	}

	@Transactional
	@RedisLock(lockKey = "user:point{#command.userId()}", params = "#command.userId()")
	public PointCommand.Detail use(PointCommand.Use command) {
		Point point = pointRepository.use(command.userId(), command.amount());
		PointHistory history = PointHistory.from(command);
		pointHistoryRepository.save(history);
		return PointCommand.Detail.of(history, point.getBalance());
	}
}
