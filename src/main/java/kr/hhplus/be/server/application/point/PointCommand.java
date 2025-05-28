package kr.hhplus.be.server.application.point;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.PointStatus;
import kr.hhplus.be.server.support.aop.lock.LockKeyPrefix;

import java.math.BigDecimal;

public class PointCommand {
	public record Detail(
		Long userId,
		Integer totalPrice,
		Integer amount,
		PointStatus status
	){
		public static Detail of(PointHistory history, BigDecimal totalPrice){
			return new Detail(
				history.getUserId(),
				totalPrice.intValue(),
				history.getAmount().intValue(),
				history.getStatus()
			);
		}
	}
	public record Refund(
		Long userId,
		BigDecimal totalPrice
	){
		public String getLockKey(){
			return LockKeyPrefix.USER_POINT.createKey(userId);
		}
		public static Refund of(Long userId, BigDecimal totalPrice){
			return new Refund(userId, totalPrice);
		}
	}
	public record Charge(
		Long userId,
		BigDecimal amount,
		PointStatus status
	){
		public String getLockKey(){
			return LockKeyPrefix.USER_POINT.createKey(userId);
		}
		public static Charge of(Long userId, BigDecimal amount){
			return new Charge(userId, amount,PointStatus.CHARGED);
		}
	}
	public record Use(
		Long userId,
		BigDecimal amount,
		PointStatus status
	){
		public String getLockKey(){
			return LockKeyPrefix.USER_POINT.createKey(userId);
		}
		public static Use of(Long userId, BigDecimal amount){
			return new Use(userId, amount,PointStatus.USED);
		}
	}
}
