package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointFacade {

	private final PointService pointService;
	private final UserService userService;

	public UserPointResult charge(UpdatePointCriteria.Charge criteria){
		User user = userService.get(criteria.getUserId());
		PointHistory history = pointService.charge(criteria.toCommand(user));
		return new UserPointResult(
			user.getId(),
			history.getStatus(),
			criteria.getAmount(),
			history.getPrice()
		);
	}

	public UserPointResult use(UpdatePointCriteria.Use criteria){
		User user = userService.get(criteria.getUserId());
		PointHistory history = pointService.use(criteria.toCommand(user));
		return new UserPointResult(
			user.getId(),
			history.getStatus(),
			criteria.getAmount(),
			history.getPrice()
		);
	}
}
