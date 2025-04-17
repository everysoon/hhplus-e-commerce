package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PointFacade {

	private final PointService pointService;
	private final UserService userService;

	public void charge(UpdatePointCriteria.Charge criteria){
		User user = userService.get(criteria.getUserId());
		pointService.charge(criteria.toCommand(user));
	}

	public void use(UpdatePointCriteria.Use criteria){
		User user = userService.get(criteria.getUserId());
		pointService.use(criteria.toCommand(user));
	}
}
