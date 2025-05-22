package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.support.utils.LockKeyPrefix;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public class ProductCommand {
	public record Refund(
		List<OrderItem> orderItems,
		Long orderId
	) {
		public static Refund of(Order order) {
			return new Refund(order.getOrderItems(), order.getId());
		}
		public static Refund of(List<OrderItem> orderItem, Long orderId) {
			return new Refund(orderItem, orderId);
		}

		public String getLockKey() {
			return LockKeyPrefix.ORDER_CANCEL.createKey(orderId);
		}

		public static Refund of(List<OrderItem> orderItems) {
			return new Refund(orderItems, null);
		}
	}

	public record FilterSearch(
		String name,
		String category,
		String sortBy, // CATEGORY, PRICE, LATEST(최신순)
		String sorted, // DESC, ASC
		boolean soldOut
	) {
		public static FilterSearch of(String name, String category, String sortBy, String sorted, boolean soldOut) {
			return new FilterSearch(name, category, sortBy, sorted, soldOut);
		}
	}

	public record TopSelling(
		LocalDateTime startDate,
		LocalDateTime endDate,
		Pageable pageable
	) {
		public LocalDateTime getStartDateOrDefault() {
			return startDate != null ? startDate : LocalDateTime.now().minusDays(3);
		}

		public LocalDateTime getEndDateOrDefault() {
			return endDate != null ? endDate : LocalDateTime.now();
		}
	}
}
