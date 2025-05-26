package kr.hhplus.be.server.application.order;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public record CancelOrderPaidFailEvent(
	Long userId,
	Long orderId,
	BigDecimal totalPrice,
	Exception exception

) {
	public static CancelOrderPaidFailEvent of(Long userId, Long orderId, BigDecimal totalPrice, Exception exception) {
		return new CancelOrderPaidFailEvent(
			userId,
			orderId,
			totalPrice,
			exception
		);
	}

	public Object getEventKey() {
		Map<String,String> map = new HashMap<>();
		map.put("userId", userId.toString());
		map.put("orderId", orderId.toString());
		map.put("totalPrice", totalPrice.toString());
		return map;
	}
}
