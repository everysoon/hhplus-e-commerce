package kr.hhplus.be.server.application.order;

public record OrderPaidFailEvent(
	Long orderId,
	Long userId
) {
	public static OrderPaidFailEvent of(Long orderId,Long userId){
		return new OrderPaidFailEvent(
			orderId,
			userId
		);
	}
}
