package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {

	@Override
	public List<OrderHistory> findByUserId(Long userId) {
		return List.of();
	}
}
