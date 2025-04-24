package kr.hhplus.be.server.interfaces.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.infra.payment.entity.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
		List<String> couponId;
	}

	public record CancelResponse(
		Long userId,
		Long orderId,
		Long paymentId,
		Integer totalPrice,
		PaymentMethod method
	) {

		public static CancelResponse of(OrderResult.Cancel result) {
			return new CancelResponse(
				result.order().getUserId(),
				result.order().getId(),
				result.payment().getId(),
				result.order().getTotalPrice().intValue(),
				result.payment().getPaymentMethod()
			);
		}
	}

	public record OrderResponse(
		Long userId,
		List<ProductDTO.ProductResponse> orderItems,
		PaymentMethod paymentMethod,
		PaymentStatus paymentStatus,
		Integer totalPrice,
		Integer totalDiscount,
		LocalDateTime orderedAt
	) {

		public static OrderResponse of(OrderResult.Place result) {
			return new OrderResponse(
				result.userId(),
				result.products().stream()
					.map(ProductDTO.ProductResponse::from).toList(),
				result.payment().getPaymentMethod(),
				result.payment().getStatus(),
				result.order().getTotalPrice().intValue(),
				result.order().getTotalDiscount().intValue(),
				result.order().getOrderedAt()
			);
		}
	}

	public record UserOrderResponse(
		Long userId,
		List<OrderDetailResponse> orderInfos
	) {

		public static UserOrderResponse of(OrderResult.InfoByUser result) {
			return new UserOrderResponse(
				result.userId(),
				result.results().stream().map(OrderDetailResponse::of).toList()
			);
		}
	}

	public record OrderDetailResponse(
		Long orderId,
		List<ProductDTO.OrderItemDetailResponse> orderItems,
		List<CouponDTO.OrderCouponResponse> coupons

	) {

		public static OrderDetailResponse of(OrderResult.DetailByOrder result) {
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
	public static class OrderItemRequest {

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
	) {

	}
}
