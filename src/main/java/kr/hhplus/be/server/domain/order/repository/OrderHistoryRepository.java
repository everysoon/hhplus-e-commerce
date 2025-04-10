package kr.hhplus.be.server.domain.order.repository;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderHistory;

public interface OrderHistoryRepository {
	List<OrderHistory> findByUserId(Long userId);
}
