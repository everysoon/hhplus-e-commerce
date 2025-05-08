package kr.hhplus.be.server.application.point;

import java.math.BigDecimal;

public class PointCriteria {
	public record Charge(Long userId, BigDecimal amount){
		public static Charge of(Long userId, BigDecimal amount){
			return new Charge(userId, amount);
		}
		public PointCommand.Charge toCommand(){
			return PointCommand.Charge.of(userId, amount);
		}
	}
	public record Use(Long userId, BigDecimal amount){
		public static Use of(Long userId, BigDecimal amount){
			return new Use(userId, amount);
		}
		public PointCommand.Use toCommand(){
			return PointCommand.Use.of(userId, amount);
		}
	}
}
