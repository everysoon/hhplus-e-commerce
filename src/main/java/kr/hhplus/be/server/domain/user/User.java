package kr.hhplus.be.server.domain.user;

import java.time.LocalDateTime;
import kr.hhplus.be.server.infra.user.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class User {

	private final Long id;
	private final String email;
	private final String address;
	private final String name;
	private final LocalDateTime createdAt;

	public static User from(UserEntity entity) {
		return new User(
			entity.getId(),
			entity.getEmail(),
			entity.getAddress(),
			entity.getName(),
			entity.getCreatedAt()
		);
	}
}
