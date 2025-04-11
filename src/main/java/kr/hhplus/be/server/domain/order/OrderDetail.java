package kr.hhplus.be.server.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.infra.product.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderDetail {
	private final Long userId;
	private final List<OrderItem> orderItems;
	private final List<OrderHistory> orderHistories;

	public static OrderDetail from(Long userId, List<OrderItem> orderItems,List<OrderHistory> histories) {
		return new  OrderDetail(
			userId,
			orderItems,
			histories
		);
	}
}
