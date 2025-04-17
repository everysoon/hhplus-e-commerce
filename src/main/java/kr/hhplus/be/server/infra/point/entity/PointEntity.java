package kr.hhplus.be.server.infra.point.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.infra.user.entity.UserEntity;
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

	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id",  foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT) )
	private UserEntity user;

	private BigDecimal balance;
	public  PointEntity(User user, BigDecimal balance) {
		this.user = UserEntity.from(user);
		this.balance = balance;
	}
	public static   PointEntity from(Point point) {
		return new PointEntity(
			point.getUser(),
			point.getBalance()
		);
	}
	public Point toDomain(){
		return new Point(
			this.id,
			this.user.toDomain(),
			this.balance
		);
	}
}
