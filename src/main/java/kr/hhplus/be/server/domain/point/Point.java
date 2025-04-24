package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class Point {

	private final Long id;

	private Long userId;

	private BigDecimal balance;

	public static Point create(Long userId) {
		return new Point(
			null,
			userId,
			BigDecimal.ZERO
		);
	}

	public static Point from(PointCommand.Charge command) {
		return new Point(
			null,
			command.userId(),
			command.amount()
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
