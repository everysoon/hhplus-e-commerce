package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.domain.user.repository.PointHistoryRepository;
import kr.hhplus.be.server.infra.point.entity.PointStatus;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;
	private final PointHistoryRepository pointHistoryRepository;

	public void refund(UpdatePointCommand.Refund command) {
		PointHistory pointHistory = new PointHistory(null, command.getUser().getId(), PointStatus.REFUND, command.getTotalPrice(), LocalDateTime.now());
		Point point = pointRepository.findByUserId(command.getUser().getId()).get();
		point.charge(command.getTotalPrice());
		pointRepository.save(point);
		pointHistoryRepository.save(pointHistory);
	}

	public PointHistory charge(UpdatePointCommand.Charge command) {
		PointHistory history = PointHistory.from(command);
		pointRepository.findByUserId(command.getUser().getId())
			.ifPresentOrElse(
				newPoint -> newPoint.charge(command.getAmount()),
				() -> pointRepository.save(Point.from(command))
			);
		return pointHistoryRepository.save(history);
	}

	public Point getUserPoint(Long userId) {
		return pointRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.NOT_EXIST_POINT_BY_USER_ID));
	}

	public PointHistory use(UpdatePointCommand.Use command) {
		PointHistory history = PointHistory.from(command);
		pointRepository.findByUserId(command.getUser().getId())
			.ifPresentOrElse(
				balance -> balance.use(command.getAmount()),
				() -> {
					throw new CustomException(ErrorCode.INSUFFICIENT_POINTS);
				}
			);
		return pointHistoryRepository.save(history);
	}
}
