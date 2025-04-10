package kr.hhplus.be.server.application.order.service;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import kr.hhplus.be.server.domain.order.repository.OrderItemRepository;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {
	private final OrderItemRepository orderItemRepository;

	public List<OrderItem> findByOrderId(Long orderId){
		return orderItemRepository.findByOrderId(orderId).stream().map(OrderItem::from).toList();
	}
}
