package kr.hhplus.be.server.application.user.dto;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.user.User;


public record UserResponseDTO(
	Long id,
	BigDecimal point,
	String name,
	String address,
	String email
) {
	public static UserResponseDTO from(User user) {
		return new UserResponseDTO(
			user.getId(),
			user.getPoint(),
			user.getName(),
			user.getAddress(),
			user.getEmail()
		);
	}
}
