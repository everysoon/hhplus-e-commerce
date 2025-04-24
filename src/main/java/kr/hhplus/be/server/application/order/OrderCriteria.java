package kr.hhplus.be.server.application.order;

import java.util.List;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.interfaces.dto.OrderDTO;

public class OrderCriteria {
	public record Cancel(
		Long userId,
		Long orderId
	) {
		public static Cancel from(Long userId, Long orderId) {
			return new Cancel(userId,orderId);
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
