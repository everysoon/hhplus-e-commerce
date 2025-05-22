package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.*;
import kr.hhplus.be.server.infra.lock.RedisLock;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;
	private final PointHistoryRepository pointHistoryRepository;

	@Transactional(propagation = MANDATORY)
	@RedisLock(lockKey = "#command.getLockKey()", waitTime = 200)
	public Point refund(PointCommand.Refund command) {
		PointHistory history = new PointHistory(command.userId(), PointStatus.REFUND, command.totalPrice());
		Point point = pointRepository.charge(command.userId(), command.totalPrice());
		pointHistoryRepository.save(history);
		return point;
	}

	@Transactional
	@RedisLock(lockKey = "#command.getLockKey()", waitTime = 500)
	public PointCommand.Detail charge(PointCommand.Charge command) {
		Point point = pointRepository.charge(command.userId(), command.amount());
		PointHistory history = new PointHistory(command.userId(), PointStatus.CHARGED, command.amount());
		pointHistoryRepository.save(history);
		return PointCommand.Detail.of(history, point.getBalance());
	}

	@Transactional(readOnly = true)
	public Point getUserPoint(Long userId) {
		return pointRepository.findByUserId(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_POINT_BY_USER_ID));
	}

	@Transactional
	@RedisLock(lockKey = "#command.getLockKey()", waitTime = 500)
	public PointCommand.Detail use(PointCommand.Use command) {
		Point point = pointRepository.use(command.userId(), command.amount());
		PointHistory history = new PointHistory(command.userId(), PointStatus.USED, command.amount());
		pointHistoryRepository.save(history);
		return PointCommand.Detail.of(history, point.getBalance());
	}
}
