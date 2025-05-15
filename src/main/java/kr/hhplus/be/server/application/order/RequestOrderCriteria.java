package kr.hhplus.be.server.application.order;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import kr.hhplus.be.server.application.coupon.CouponValidCommand;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.interfaces.dto.OrderDTO;
import org.springframework.validation.annotation.Validated;

@Validated
public record RequestOrderCriteria(
	@NotNull Long userId,
	@NotNull List<Item> orderItems,
	List<String> couponIds,
	PaymentMethod paymentMethod

) {
	public static RequestOrderCriteria from(OrderDTO.OrderRequest dto) {
		List<Item> items = dto.getProducts().stream()
			.map(p -> new Item(p.getProductId(), p.getQuantity()))
			.toList();

		return new RequestOrderCriteria(dto.getUserId(), items, dto.getCouponId(),PaymentMethod.POINTS);
	}
	public CouponValidCommand toCouponValidCommand(){
		return CouponValidCommand.of(userId(),couponIds());
	}
	public record Item(
		Long productId,
		int quantity
	) {
	}

}
