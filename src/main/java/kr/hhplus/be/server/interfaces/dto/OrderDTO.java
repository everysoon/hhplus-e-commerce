package kr.hhplus.be.server.interfaces.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.application.order.OrderResult;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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
		Integer totalPrice
	) {

		public static CancelResponse of(OrderResult.Cancel result) {
			return new CancelResponse(
				result.order().getUserId(),
				result.order().getId(),
				result.order().getTotalPrice().intValue()
			);
		}
	}

	public record OrderResponse(
		Long userId,
		List<Long> orderProductIds,
		Integer totalPrice,
		Integer totalDiscount,
		LocalDateTime orderedAt
	) {

		public static OrderResponse of(OrderResult.Place result) {
			return new OrderResponse(
				result.userId(),
				result.productIds(),
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
		List<Long> orderProductIds,
		List<String> coupons

	) {

		public static OrderDetailResponse of(OrderResult.DetailByOrder result) {
			return new OrderDetailResponse(
				result.orderId(),
				result.productIds(),
				result.couponIds()
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
