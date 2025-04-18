package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.NOT_EXIST_ORDER;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.UNAUTHORIZED_ORDER_ACCESS;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {

	private final OrderJpaRepository orderJpaRepository;

	@Override
	public Order save(Order order) {
		try{
			OrderEntity orderEntity = orderJpaRepository.saveAndFlush(OrderEntity.from(order));
			return orderEntity.toDomain();
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public Order findById(Long orderId) {
		return orderJpaRepository.findById(orderId)
			.orElseThrow(() -> new CustomException(NOT_EXIST_ORDER))
			.toDomain();
	}

	@Override
	public Order findByIdAndUserId(Long orderId, Long userId) {
		OrderEntity orderEntity = orderJpaRepository.findByIdAndUserId(orderId, userId);
		if (orderEntity == null) {
			throw new CustomException(UNAUTHORIZED_ORDER_ACCESS);
		}
		return orderEntity.toDomain();
	}

	@Override
	public List<Order> findByUserId(Long userId) {
		return orderJpaRepository.findByUserId(userId).stream().map(OrderEntity::toDomain).toList();
	}

	@Override
	public List<Order> findAll() {
		return orderJpaRepository.findAll().stream().map(OrderEntity::toDomain).toList();
	}
}
