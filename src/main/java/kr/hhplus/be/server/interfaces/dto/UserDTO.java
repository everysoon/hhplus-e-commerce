package kr.hhplus.be.server.interfaces.dto;

import kr.hhplus.be.server.domain.point.Point;

import java.math.BigDecimal;

public class UserDTO {
	public record UserResponse(
		Long id,
		BigDecimal point,
		String name,
		String address,
		String email
	){
		public static UserResponse from(Point point) {
			return new UserResponse(
				point.getUser().getId(),
				point.getBalance(),
				point.getUser().getName(),
				point.getUser().getAddress(),
				point.getUser().getEmail()
			);
		}
	}
//	public record UserOrderResponse(
//		Long userId,
//		List<OrderDTO.OrderItemResponse> orderItems
//	){}
}
