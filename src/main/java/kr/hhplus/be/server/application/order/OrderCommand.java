package kr.hhplus.be.server.application.order;

import jakarta.validation.constraints.NotNull;
import kr.hhplus.be.server.interfaces.dto.OrderDTO;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Validated
public record OrderCommand(
	@NotNull Long userId,
	@NotNull List<Item> orderItems,
	List<UUID> couponIds
) {
	public static OrderCommand from(OrderDTO.OrderRequest dto) {
		List<Item> items = dto.getProducts().stream()
			.map(p -> new Item(p.getProductId(), p.getQuantity()))
			.toList();

		return new OrderCommand(dto.getUserId(), items, dto.getCouponId());
	}
	public record Item(
		Long productId,
		int quantity
	) {
	}

}
