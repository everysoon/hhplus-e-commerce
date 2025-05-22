package kr.hhplus.be.server.infra.order;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.NOT_EXIST_ORDER;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.UNAUTHORIZED_ORDER_ACCESS;

@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;

	@Override
	public Order save(Order order) {
		return orderJpaRepository.saveAndFlush(order);
	}

	@Override
	public List<Order> saveAll(List<Order> order) {
		return orderJpaRepository.saveAllAndFlush(order);
	}

	@Override
	public Order findById(Long orderId) {
		return orderJpaRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(NOT_EXIST_ORDER));
	}

	@Override
	public Order findByIdAndUserId(Long orderId, Long userId) {
		return orderJpaRepository.findByIdAndUserId(orderId, userId).orElseThrow(()->new CustomException(UNAUTHORIZED_ORDER_ACCESS));
	}

	@Override
	public List<Order> findByUserId(Long userId) {
		return orderJpaRepository.findByUserId(userId);
	}

	@Override
	public List<Order> findAll() {
		return orderJpaRepository.findAll();
	}

	@Override
	public Order existsOrder(Long userId, List<Long> productIds) {
		return orderJpaRepository.existsOrder(userId, productIds);
	}
}
