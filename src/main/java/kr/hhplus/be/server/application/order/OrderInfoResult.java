package kr.hhplus.be.server.application.order;

import java.util.List;

public record OrderInfoResult (
	Long userId,
	List<OrderDetailResult> results
){

	public static OrderInfoResult from(Long userId, List<OrderDetailResult> results) {
		return new OrderInfoResult(
			userId,
			results
		);
	}
}
