package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.infra.point.entity.PointStatus;

import java.math.BigDecimal;

public class PointCommand {
	public record Refund(
		Long userId,
		BigDecimal totalPrice
	){
		public static Refund of(Long userId, BigDecimal totalPrice){
			return new Refund(userId, totalPrice);
		}
	}
	public record Charge(
		Long userId,
		BigDecimal amount,
		PointStatus status
	){
		public static Charge of(Long userId, BigDecimal amount){
			return new Charge(userId, amount,PointStatus.CHARGED);
		}
	}
	public record Use(
		Long userId,
		BigDecimal amount,
		PointStatus status
	){
		public static Use of(Long userId, BigDecimal amount){
			return new Use(userId, amount,PointStatus.USED);
		}
	}
}
