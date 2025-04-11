package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.repository.OrderItemRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderItemRepositoryImpl implements OrderItemRepository {

	@Override
	public List<OrderItem> findByOrderId(Long orderId) {
		return List.of();
	}

	@Override
	public List<OrderItem> saveAll(List<OrderItem> orderItems) {
		return List.of();
	}
}
