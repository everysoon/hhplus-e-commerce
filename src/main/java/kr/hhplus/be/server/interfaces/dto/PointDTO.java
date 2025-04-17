package kr.hhplus.be.server.interfaces.dto;

import kr.hhplus.be.server.application.point.UserPointResult;
import kr.hhplus.be.server.infra.point.entity.PointStatus;

import java.math.BigDecimal;

public class PointDTO {
	public record UserPointResponse(
		Long userId,
		PointStatus status,
		BigDecimal amount,
		BigDecimal currentBalance
	){
		public static UserPointResponse from(UserPointResult result) {
			return new UserPointResponse(
				result.userId(),
				result.status(),
				result.amount(),
				result.currentBalance()
			);
		}
	}
}
