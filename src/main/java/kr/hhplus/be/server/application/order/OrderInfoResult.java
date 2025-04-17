package kr.hhplus.be.server.application.order;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;

public record OrderInfoResult (
	Long userId,
	List<OrderItem> orderItems,
	List<OrderHistory> orderHistories
){

	public static OrderInfoResult from(Long userId, List<OrderItem> orderItems,List<OrderHistory> histories) {
		return new OrderInfoResult(
			userId,
			orderItems,
			histories
		);
	}
}
