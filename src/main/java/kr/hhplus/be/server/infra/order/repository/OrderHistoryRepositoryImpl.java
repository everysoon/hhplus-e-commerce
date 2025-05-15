package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import kr.hhplus.be.server.infra.order.entity.OrderHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderHistoryRepositoryImpl implements OrderHistoryRepository {
	private final OrderHistoryJpaRepository orderHistoryJpaRepository;

	@Override
	public List<OrderHistory> findByUserId(Long userId) {
		return orderHistoryJpaRepository.findByUserId(userId)
			.stream()
			.map(OrderHistoryEntity::toDomain).toList();
	}

	@Override
	public OrderHistory save(OrderHistory orderHistory) {
		return orderHistoryJpaRepository.save(OrderHistoryEntity.from(orderHistory)).toDomain();
	}
}
