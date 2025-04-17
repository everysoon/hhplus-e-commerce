package kr.hhplus.be.server.application.order;

public record CancelOrderCriteria(
	Long userId,
	Long orderId
) {
	public static CancelOrderCriteria from(Long userId, Long orderId) {
		return new CancelOrderCriteria(userId,orderId);
	}
}
