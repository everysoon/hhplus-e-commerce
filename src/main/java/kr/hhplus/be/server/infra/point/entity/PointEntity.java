package kr.hhplus.be.server.infra.point.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.point.Point;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "points")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(updatable = false, nullable = false)
	private Long id;

	private Long userId;

	private BigDecimal balance;

	public PointEntity(Long userId, BigDecimal balance) {
		this.userId = userId;
		this.balance = balance;
	}

	public PointEntity(Long id,Long userId, BigDecimal balance) {
		this(userId, balance);
		this.id = id;
	}

	public static PointEntity from(Point point) {
		return new PointEntity(
			point.getId(),
			point.getUserId(),
			point.getBalance()
		);
	}

	public Point toDomain() {
		return new Point(
			this.id,
			this.userId,
			this.balance
		);
	}
}
