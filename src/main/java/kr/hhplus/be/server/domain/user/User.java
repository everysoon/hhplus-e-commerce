package kr.hhplus.be.server.domain.user;

import kr.hhplus.be.server.infra.user.entity.UserEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class User {

	private final Long id;
	private BigDecimal point;
	private final String email;
	private final String address;
	private final String name;
	private final String password;
	private final LocalDateTime createdAt;

	public void charge(BigDecimal price) {
		this.point = this.point.add(price);
	}

	public void use(BigDecimal price) {
		if(price.compareTo(this.point) <= 0) {
			throw new CustomException(ErrorCode.INSUFFICIENT_POINTS);
		}
		this.point = this.point.subtract(price);
	}

	public static User from(UserEntity entity) {
		return new User(
			entity.getId(),
			entity.getPoint(),
			entity.getEmail(),
			entity.getAddress(),
			entity.getName(),
			entity.getPassword(),
			entity.getCreatedAt()
		);
	}
}
