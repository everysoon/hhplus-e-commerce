package kr.hhplus.be.server.domain.order.repository;

import kr.hhplus.be.server.domain.order.OrderHistory;

import java.util.List;

public interface OrderHistoryRepository {
	List<OrderHistory> findByUserId(Long userId);
	OrderHistory save(OrderHistory orderHistory);
}
