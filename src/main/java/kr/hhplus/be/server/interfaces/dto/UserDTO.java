package kr.hhplus.be.server.interfaces.dto;

import kr.hhplus.be.server.domain.point.Point;

public class UserDTO {
	public record UserResponse(
		Long id,
		Integer point
	){
		public static UserResponse from(Point point) {
			return new UserResponse(
				point.getUserId(),
				point.getBalance().intValue()
			);
		}
	}
}
