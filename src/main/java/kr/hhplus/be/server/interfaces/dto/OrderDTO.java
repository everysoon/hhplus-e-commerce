package kr.hhplus.be.server.interfaces.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.application.order.OrderDetailResult;
import kr.hhplus.be.server.application.order.OrderInfoResult;
import kr.hhplus.be.server.application.order.PlaceOrderResult;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.infra.payment.entity.PaymentStatus;
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
		BigDecimal totalDiscount,
		LocalDateTime orderedAt
	) {
		public static OrderResponse of(PlaceOrderResult result) {
			return new OrderResponse(
				result.userId(),
				result.products().stream()
					.map(ProductDTO.ProductResponse::from).toList(),
				result.payment().getPaymentMethod(),
				result.payment().getStatus(),
				result.order().getTotalPrice(),
				result.order().getTotalDiscount(),
				result.order().getOrderedAt()
			);
		}
	}
	public record UserOrderResponse(
		Long userId,
		List<OrderDetailResponse> orderInfos
	){
		public static UserOrderResponse of(OrderInfoResult result){
			return new UserOrderResponse(
				result.userId(),
				result.results().stream().map(OrderDetailResponse::of).toList()
			);
		}
	}
	public record OrderDetailResponse(
		Long orderId,
		List<ProductDTO.OrderItemDetailResponse>  orderItems,
		List<CouponDTO.OrderCouponResponse> coupons

	){
		public static OrderDetailResponse of(OrderDetailResult result){
			return new OrderDetailResponse(
				result.orderId(),
				result.productList().stream().map(ProductDTO.OrderItemDetailResponse::of).toList(),
				result.coupons().stream().map(CouponDTO.OrderCouponResponse::of).toList()
			);
		}
	}
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	public class OrderItemRequest {
		@NotNull
		Long productId;
		@NotNull
		@Min(1)
		Integer quantity;
	}
	public record OrderItemResponse(
		ProductDTO.ProductResponse item,
		Long orderId,
		LocalDateTime createdAt
	){

	}

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
