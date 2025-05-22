package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;

import java.util.List;

public class OrderResult {
	public record Cancel(
		Order order
	) {
		public static Cancel of(Order order) {
			return new Cancel(order);
		}
	}
	public record DetailByOrder(
		Long orderId,
		List<Long> productIds,
		List<String> couponIds
	){
		public static DetailByOrder from(Order order) {
			return new DetailByOrder(
				order.getId(),
				order.getOrderItems().stream().map(OrderItem::getProductId).toList(),
				order.getUsedUserCouponIds()
			);
		}
	}
	public record InfoByUser (
		Long userId,
		List<OrderResult.DetailByOrder> results
	){

		public static InfoByUser from(Long userId, List<OrderResult.DetailByOrder> results) {
			return new InfoByUser(
				userId,
				results
			);
		}
	}
	public record Place(
		Long userId,
		List<Long> productIds,
		Order order
	) {
		public static Place of(Long userId, List<Long> productIds, Order order) {
			return new Place(
				userId,
				productIds,
				order
			);
		}
	}

}
