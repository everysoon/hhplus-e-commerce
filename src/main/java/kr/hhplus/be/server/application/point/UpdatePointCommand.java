package kr.hhplus.be.server.application.point;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.point.entity.PointStatus;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UpdatePointCommand {

	@Getter
	public static class Charge {

		private final User user;
		private final BigDecimal amount;
		private final PointStatus status;

		private Charge(User user, BigDecimal amount, PointStatus status) {
			this.user=user;
			this.amount = amount;
			this.status = status;
		}

		public static Charge of(User user, BigDecimal amount) {
			return new Charge(
				user,
				amount,
				PointStatus.CHARGED
			);
		}
	}
	@Getter
	public static class Use {

		private final User user;
		private final BigDecimal amount;
		private PointStatus status;

		private Use(User user, BigDecimal amount, PointStatus status) {
			this.user = user;
			this.amount = amount;
			this.status = status;
		}
		public static Use of(User user, BigDecimal amount) {
			return new Use(
				user,
				amount,
				PointStatus.USED
			);
		}
	}
}
