package kr.hhplus.be.server.application.point;

import java.math.BigDecimal;
import kr.hhplus.be.server.infra.point.entity.PointStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdatePointCommand {
	@Getter
	public static class Refund{
		private final Long userId;
		private final BigDecimal totalPrice;
		private Refund(Long userId, BigDecimal totalPrice) {
			this.userId = userId;
			this.totalPrice = totalPrice;
		}
		public static Refund of(Long userId, BigDecimal totalPrice) {
			return new Refund(userId, totalPrice);
		}
	}
	@Getter
	public static class Charge {

		private final Long userId;
		private final BigDecimal amount;
		private final PointStatus status;

		private Charge(Long userId, BigDecimal amount, PointStatus status) {
			this.userId = userId;
			this.amount = amount;
			this.status = status;
		}

		public static Charge of(Long userId, BigDecimal amount) {
			return new Charge(
				userId,
				amount,
				PointStatus.CHARGED
			);
		}
	}
	@Getter
	public static class Use {

		private final Long userId;
		private final BigDecimal amount;
		private PointStatus status;

		private Use(Long userId, BigDecimal amount, PointStatus status) {
			this.userId = userId;
			this.amount = amount;
			this.status = status;
		}
		public static Use of(Long userId, BigDecimal amount) {
			return new Use(
				userId,
				amount,
				PointStatus.USED
			);
		}
	}
}
