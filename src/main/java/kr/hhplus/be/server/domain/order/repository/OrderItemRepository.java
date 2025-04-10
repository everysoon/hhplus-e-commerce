package kr.hhplus.be.server.domain.order.repository;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.infra.order.entity.OrderItemEntity;

public interface OrderItemRepository {
	List<OrderItem> findByOrderId(Long orderId);
}
