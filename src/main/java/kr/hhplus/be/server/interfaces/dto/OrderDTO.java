package kr.hhplus.be.server.interfaces.dto;

import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public class OrderDTO {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class OrderRequest {

		@NotNull
		Long userId;
		@NotNull
		List<OrderItemRequest> products;
		List<UUID> couponId;
	}

	public record OrderResponse(
		Long userId,
		List<ProductDTO.ProductResponse> orderItems,
		PaymentMethod paymentMethod,
		PaymentStatus paymentStatus,
		BigDecimal totalPrice,
		BigDecimal couponDiscountAmount,
		LocalDateTime orderedAt,
		OrderStatus orderStatus
	) {

	}
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public class OrderItemRequest {
		@NotNull
		Long productId;
		@NotNull
		Integer quantity;
	}
	public record OrderItemResponse(
		ProductDTO.ProductResponse item,
		Long orderId,
		OrderStatus status,
		LocalDateTime createdAt
	){}
//	public record OrderItemResponse(
//		@NotNull List<Item> orderItems,
//		List<UUID> couponIds
//	) {
//		public static OrderItemResponse from(OrderRequest dto) {
//			List<Item> items = dto.getProducts().stream()
//				.map(p -> new Item(p.productId, p.quantity))
//				.toList();
//
//			return new OrderItemResponse(items, dto.couponId);
//		}
//		public record Item(
//			Long productId,
//			int quantity
//		) {}
//	}
}
