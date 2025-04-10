package kr.hhplus.be.server.application.order.dto;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import org.springframework.validation.annotation.Validated;


public record OrderItemResponse(
	@NotNull Long userId,
	@NotNull List<Item> orderItems,
	List<UUID> couponIds
) {
	public static OrderItemResponse from(OrderRequestDTO dto) {
		List<Item> items = dto.getProducts().stream()
			.map(p -> new Item(p.getProductId(), p.getQuantity()))
			.toList();

		return new OrderItemResponse(dto.getUserId(), items, dto.getCouponId());
	}
	public record Item(
		Long productId,
		int quantity
	) {}

}
