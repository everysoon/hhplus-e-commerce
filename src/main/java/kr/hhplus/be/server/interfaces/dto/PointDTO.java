package kr.hhplus.be.server.interfaces.dto;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.domain.point.PointStatus;

public class PointDTO {
	public record UserPointResponse(
		Long userId,
		PointStatus status,
		Integer amount,
		Integer totalPrice
	){
		public static UserPointResponse from(PointCommand.Detail detail) {
			return new UserPointResponse(
				detail.userId(),
				detail.status(),
				detail.amount().intValue(),
				detail.totalPrice().intValue()
			);
		}
	}
}
