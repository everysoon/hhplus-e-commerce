package kr.hhplus.be.server.utils;

import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.user.User;

public class UserTestFixture {
	public static User createUser(Long userId){
		return new User(
			userId,
			"vashti.nienow@hotmail.com",
			"713 Mickey Drive, Diamouth, FL 18534",
			"Rusty Schroeder IV",
			LocalDateTime.of(2025,4,7,13,26,17,675478)
		);
	}
}
