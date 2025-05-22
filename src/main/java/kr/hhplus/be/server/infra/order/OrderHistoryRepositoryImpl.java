package kr.hhplus.be.server.infra.order;

import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import kr.hhplus.be.server.domain.order.OrderHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {
	private final OrderHistoryJpaRepository orderHistoryJpaRepository;

	@Override
	public List<OrderHistory> findByUserId(Long userId) {
		return orderHistoryJpaRepository.findByUserId(userId);
	}

	@Override
	public OrderHistory save(OrderHistory orderHistory) {
		return orderHistoryJpaRepository.save(orderHistory);
	}
}
