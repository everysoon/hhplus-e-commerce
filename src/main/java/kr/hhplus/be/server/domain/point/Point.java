package kr.hhplus.be.server.domain.point;

import java.math.BigDecimal;
import kr.hhplus.be.server.application.point.UpdatePointCommand;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Point {

	private final Long id;

	private Long userId;

	private BigDecimal balance;

	public static Point from(Long userId) {
		return new Point(
			null,
			userId,
			BigDecimal.ZERO
		);
	}

	public static Point from(UpdatePointCommand.Charge command) {
		return new Point(
			null,
			command.getUserId(),
			command.getAmount()
		);
	}

	public Point charge(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) <= 0) {
			throw new CustomException(ErrorCode.INVALID_CHARGE_AMOUNT);
		}
		this.balance = this.balance.add(amount);
		return this;
	}


	public Point use(BigDecimal amount) {
		if (balance.compareTo(amount) < 0) {
			throw new CustomException(ErrorCode.INSUFFICIENT_POINTS);
		}
		balance = balance.subtract(amount);
		return this;
	}
}
