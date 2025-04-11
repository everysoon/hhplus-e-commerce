package kr.hhplus.be.server.interfaces.dto;

import kr.hhplus.be.server.domain.user.User;

import java.math.BigDecimal;

public class UserDTO {
	public record UserResponse(
		Long id,
		BigDecimal point,
		String name,
		String address,
		String email
	){
		public static UserResponse from(User user) {
			return new UserResponse(
				user.getId(),
				user.getPoint(),
				user.getName(),
				user.getAddress(),
				user.getEmail()
			);
		}
	}
//	public record UserOrderResponse(
//		Long userId,
//		List<OrderDTO.OrderItemResponse> orderItems
//	){}
}
