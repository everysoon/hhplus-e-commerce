package kr.hhplus.be.server.application.order.command;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.application.order.dto.OrderRequestDTO;
import kr.hhplus.be.server.domain.order.OrderItem;
import org.springframework.validation.annotation.Validated;

@Validated
public record OrderCommand(
	@NotNull Long userId,
	@NotNull List<Item> orderItems,
	List<UUID> couponIds
) {
	public static OrderCommand from(OrderRequestDTO dto) {
		List<Item> items = dto.products().stream()
			.map(p -> new Item(p.productId(), p.quantity()))
			.toList();

		return new OrderCommand(dto.userId(), items, dto.couponId());
	}
	public record Item(
		Long productId,
		int quantity
	) {
	}

}
