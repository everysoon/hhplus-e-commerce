package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.OrderItemMapper;
import kr.hhplus.be.server.domain.order.repository.OrderItemRepository;
import kr.hhplus.be.server.infra.order.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderItemRepositoryImpl implements OrderItemRepository {
	private final OrderItemJpaRepository orderItemJpaRepository;
	private final OrderItemMapper mapper;
	@Override
	public List<OrderItem> saveAll(List<OrderItem> orderItems) {
		List<OrderItemEntity> orderItemEntities = orderItems.stream().map(mapper::toEntity).toList();
		return orderItemJpaRepository.saveAll(orderItemEntities).stream().map(mapper::toDomain).toList();
	}

	@Override
	public List<OrderItem> findByOrderId(Long orderId) {
		return orderItemJpaRepository.findByOrderId(orderId).stream().map(mapper::toDomain).toList();
	}
}
