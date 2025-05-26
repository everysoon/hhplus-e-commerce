package kr.hhplus.be.server.domain.point;

import jakarta.persistence.*;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "points")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Point {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	private Long userId;

	private BigDecimal balance;

	public Point(Long userId) {
		this.userId = userId;
		this.balance = BigDecimal.ZERO;
	}

	public Point(Long userId, BigDecimal balance) {
		this.userId = userId;
		this.balance = balance;
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
