package kr.hhplus.be.server.application.order;

import java.util.HashMap;
import java.util.Map;

public record OrderPaidFailEvent(
	Long orderId,
	Long userId,
	Exception exception
) {
	public static OrderPaidFailEvent of(Long orderId, Long userId, Exception exception) {
		return new OrderPaidFailEvent(
			orderId,
			userId,
			exception
		);
	}

	public Object getEventKey() {
		Map<String, Long> map = new HashMap();
		map.put("orderId", orderId);
		map.put("userId", userId);
		return map;
	}
}
