package kr.hhplus.be.server.application.order.service;

import java.util.List;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderHistoryService {
	private final OrderHistoryRepository orderHistoryRepository;
	public List<OrderHistory> findByUserId(Long userId){
		return orderHistoryRepository.findByUserId(userId);
	}
}
