package kr.hhplus.be.server.application.order.service;

import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.order.repository.OrderItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderItemService {
	private final OrderItemRepository orderItemRepository;

	public List<OrderItem> findByOrderId(Long orderId){
		return orderItemRepository.findByOrderId(orderId);
	}
	public List<OrderItem> saveAll(List<OrderItem> orderItems) {
		return orderItemRepository.saveAll(orderItems);
	}
}
