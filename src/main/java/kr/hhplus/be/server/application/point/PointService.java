package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.domain.user.repository.PointHistoryRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PointService {

	private final PointRepository pointRepository;
	private final PointHistoryRepository pointHistoryRepository;

	public void charge(UpdatePointCommand.Charge command) {
		PointHistory history = PointHistory.from(command);
		pointRepository.findByUserId(command.getUser().getId())
			.ifPresentOrElse(
				newPoint -> newPoint.charge(command.getAmount()),
				() -> pointRepository.save(Point.from(command))
			);
		pointHistoryRepository.save(history);
	}

	public void use(UpdatePointCommand.Use command) {
		PointHistory history = PointHistory.from(command);
		pointRepository.findByUserId(command.getUser().getId())
			.ifPresentOrElse(
				balance -> balance.use(command.getAmount()),
				() -> {
					throw new CustomException(ErrorCode.INSUFFICIENT_POINTS);
				}
			);
		pointHistoryRepository.save(history);
	}
}
