package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.infra.point.entity.PointStatus;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;
	private final PointHistoryRepository pointHistoryRepository;

	@Transactional(propagation = MANDATORY)
	public Point refund(PointCommand.Refund command) {
		PointHistory pointHistory = new PointHistory(null, command.userId(), PointStatus.REFUND, command.totalPrice(), LocalDateTime.now());
		Point point = pointRepository.findByUserId(command.userId()).get();
		point.charge(command.totalPrice());
		Point save = pointRepository.update(point);
		pointHistoryRepository.save(pointHistory);
		return save;
	}

	@Transactional
	public PointHistory charge(PointCommand.Charge command) {
		PointHistory history = PointHistory.from(command);
		Point point = pointRepository.findByUserId(command.userId())
			.orElseGet(() -> pointRepository.save(Point.create(command.userId())));
		point.charge(command.amount());
		pointRepository.update(point);
		return pointHistoryRepository.save(history);
	}

	@Transactional(readOnly = true)
	public Point getUserPoint(Long userId) {
		return pointRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_POINT_BY_USER_ID));
	}

	@Transactional(propagation = MANDATORY)
	public PointHistory use(PointCommand.Use command) {
		PointHistory history = PointHistory.from(command);
		Point point = pointRepository.findByUserId(command.userId())
			.orElseThrow(() -> new CustomException(ErrorCode.INSUFFICIENT_POINTS));
		point.use(command.amount());
		pointRepository.update(point);
		return pointHistoryRepository.save(history);
	}
}
