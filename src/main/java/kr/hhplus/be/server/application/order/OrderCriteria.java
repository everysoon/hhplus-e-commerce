package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.interfaces.dto.OrderDTO;
import kr.hhplus.be.server.support.utils.LockKeyPrefix;

import java.util.List;

public class OrderCriteria {
	public record Cancel(
		Long userId,
		Long orderId
	) {
		public static Cancel from(Long userId, Long orderId) {
			return new Cancel(userId,orderId);
		}
		public String getLockKey(){
			return LockKeyPrefix.ORDER_CANCEL.createKey(orderId);
		}
	}
	public record Request(
		Long userId,
		List<Request.Item> orderItems,
		List<String> couponIds,
		PaymentMethod paymentMethod
	){
		public static Request from(OrderDTO.OrderRequest dto) {
			List<Request.Item> items = dto.getProducts().stream()
				.map(p -> new Request.Item(p.getProductId(), p.getQuantity()))
				.toList();

			return new Request(dto.getUserId(), items, dto.getCouponId(),PaymentMethod.POINTS);
		}
		public String getLockKey(){
			return LockKeyPrefix.ORDER.createKey(userId);
		}
		public List<Long> productIds(){
			return orderItems.stream().map(Item::productId).toList();
		}
		public record Item(
			Long productId,
			int quantity
		) {
		}
	}
}
