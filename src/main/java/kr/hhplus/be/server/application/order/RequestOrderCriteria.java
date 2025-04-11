package kr.hhplus.be.server.application.order;

import jakarta.validation.constraints.NotNull;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.application.coupon.CouponValidCommand;
import kr.hhplus.be.server.application.coupon.UseCouponCommand;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.interfaces.dto.OrderDTO;
import org.springframework.validation.annotation.Validated;

@Validated
public record RequestOrderCriteria(
	@NotNull Long userId,
	@NotNull List<Item> orderItems,
	List<UUID> couponIds,
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
	public UseCouponCommand toUseCouponCommand(User user,List<Coupon> coupons){
		return UseCouponCommand.of(user,coupons);
	}
	public record Item(
		Long productId,
		int quantity
	) {
	}

}
