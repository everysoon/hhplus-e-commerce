package kr.hhplus.be.server.application.point;

import java.math.BigDecimal;
import kr.hhplus.be.server.infra.point.entity.PointStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdatePointCriteria {

	@Getter
	public static class Charge {

		private final Long userId;
		private final BigDecimal amount;


		private Charge(Long userId, BigDecimal amount) {
			this.userId = userId;
			this.amount = amount;

		}

		public static Charge of(Long userId, BigDecimal amount) {
			return new Charge(
				userId,
				amount
			);
		}
		public UpdatePointCommand.Charge toCommand(Long userId) {
			return UpdatePointCommand.Charge.of(
				userId,
				amount
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
		public UpdatePointCommand.Use toCommand(Long userId) {
			return UpdatePointCommand.Use.of(
				userId,
				amount
			);
		}
	}
}
