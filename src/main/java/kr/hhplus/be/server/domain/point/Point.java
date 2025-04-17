package kr.hhplus.be.server.domain.point;

import java.math.BigDecimal;
import kr.hhplus.be.server.application.point.UpdatePointCommand;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class Point {

	private final Long id;

	private User user;

	private BigDecimal balance;

	public static Point from(UpdatePointCommand.Charge command) {
		return new Point(
			null,
			command.getUser(),
			command.getAmount()
		);
	}

	public void charge(BigDecimal amount) {
		if (amount.compareTo(BigDecimal.ZERO) < 0) {
			throw new CustomException(ErrorCode.INVALID_CHARGE_AMOUNT);
		}
		this.balance = this.balance.add(amount);
	}


	public void use(BigDecimal amount) {
		if (balance.compareTo(amount) < 0) {
			throw new CustomException(ErrorCode.INSUFFICIENT_POINTS);
		}
		balance = balance.subtract(amount);
	}
}
