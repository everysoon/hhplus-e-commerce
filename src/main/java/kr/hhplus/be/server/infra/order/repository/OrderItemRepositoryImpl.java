package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.repository.OrderItemRepository;
import kr.hhplus.be.server.infra.order.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {
	private final OrderItemJpaRepository orderItemJpaRepository;

	@Override
	public List<OrderItem> saveAll(List<OrderItem> orderItems) {
		List<OrderItemEntity> orderItemEntities = orderItems.stream().map(OrderItemEntity::from).toList();
		return orderItemJpaRepository.saveAll(orderItemEntities).stream().map(OrderItemEntity::toDomain).toList();
	}
}
