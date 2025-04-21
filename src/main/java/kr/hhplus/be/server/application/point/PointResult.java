package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.infra.point.entity.PointStatus;

import java.math.BigDecimal;

public class PointResult {
	public record UserPoint(
		Long userId,
		PointStatus status,
		BigDecimal amount,
		BigDecimal currentBalance
	){}
}
