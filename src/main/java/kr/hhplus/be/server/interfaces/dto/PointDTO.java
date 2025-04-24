package kr.hhplus.be.server.interfaces.dto;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.infra.point.entity.PointStatus;

import java.math.BigDecimal;

public class PointDTO {
	public record UserPointResponse(
		Long userId,
		PointStatus status,
		BigDecimal amount
	){
		public static UserPointResponse from(PointHistory history) {
			return new UserPointResponse(
				history.getUserId(),
				history.getStatus(),
				history.getPrice()
			);
		}
	}
}
