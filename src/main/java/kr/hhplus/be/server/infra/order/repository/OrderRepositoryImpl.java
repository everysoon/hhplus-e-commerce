package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.NOT_EXIST_ORDER;

@Repository
@RequiredArgsConstructor
public class OrderRepositoryImpl implements OrderRepository {
	private final OrderJpaRepository orderJpaRepository;
	@Override
	public Order save(Order order) {
		return orderJpaRepository.save(OrderEntity.from(order)).toDomain();
	}

	@Override
	public Order findById(Long orderId) {
		return orderJpaRepository.findById(orderId)
			.orElseThrow(()->new CustomException(NOT_EXIST_ORDER))
			.toDomain();
	}
}
