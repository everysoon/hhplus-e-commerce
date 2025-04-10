package kr.hhplus.be.server.infra.order.repository;

import java.util.List;
import kr.hhplus.be.server.config.jpa.InMemoryRepository;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.repository.OrderItemRepository;
import kr.hhplus.be.server.infra.order.entity.OrderItemEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderItemRepositoryImpl extends InMemoryRepository<Long, OrderItemEntity> implements
	OrderItemRepository {

	@Override
	public List<OrderItem> findByOrderId(Long orderId) {
		return List.of();
	}
}
