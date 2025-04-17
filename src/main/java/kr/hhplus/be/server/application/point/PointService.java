package kr.hhplus.be.server.application.point;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.infra.point.entity.PointStatus;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;
	private final PointHistoryRepository pointHistoryRepository;

	@Transactional
	public Point refund(UpdatePointCommand.Refund command) {
		PointHistory pointHistory = new PointHistory(null, command.getUserId(), PointStatus.REFUND, command.getTotalPrice(), LocalDateTime.now());
		Point point = pointRepository.findByUserId(command.getUserId()).get();
		point.charge(command.getTotalPrice());
		Point save = pointRepository.update(point);
		pointHistoryRepository.save(pointHistory);
		return save;
	}

	@Transactional
	public PointHistory charge(UpdatePointCommand.Charge command) {
		PointHistory history = PointHistory.from(command);
		Point point = pointRepository.findByUserId(command.getUserId())
			.orElseGet(() -> pointRepository.save(Point.from(command.getUserId())));
		point.charge(command.getAmount());
		pointRepository.update(point);
		return pointHistoryRepository.save(history);
	}

	public Point getUserPoint(Long userId) {
		return pointRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_POINT_BY_USER_ID));
	}
	@Transactional
	public PointHistory use(UpdatePointCommand.Use command) {
		PointHistory history = PointHistory.from(command);
		Point point = pointRepository.findByUserId(command.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.INSUFFICIENT_POINTS));
		point.use(command.getAmount());
		pointRepository.update(point);
		return pointHistoryRepository.save(history);
	}
}
