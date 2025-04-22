package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderMapper;
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
	private final OrderMapper mapper;
	@Override
	public Order save(Order order) {
		try{
			OrderEntity entity = mapper.toEntity(order);
			orderJpaRepository.saveAndFlush(entity);
			return mapper.toDomain(entity);
		}catch (Exception e){
			System.out.println(e.getMessage());
		}
		return null;
	}

	@Override
	public List<Order> saveAll(List<Order> order) {
		List<OrderEntity> list = order.stream().map(mapper::toEntity).toList();
		return orderJpaRepository.saveAllAndFlush(list).stream().map(mapper::toDomain).toList();
	}

	@Override
	public Order findById(Long orderId) {
		return orderJpaRepository.findById(orderId)
			.map(mapper::toDomain)
			.orElseThrow(() -> new CustomException(NOT_EXIST_ORDER));
	}

	@Override
	public Order findByIdAndUserId(Long orderId, Long userId) {
		OrderEntity orderEntity = orderJpaRepository.findByIdAndUserId(orderId, userId);
		if (orderEntity == null) {
			throw new CustomException(UNAUTHORIZED_ORDER_ACCESS);
		}
		return mapper.toDomain(orderEntity);
	}

	@Override
	public List<Order> findByUserId(Long userId) {
		return orderJpaRepository.findByUserId(userId).stream().map(mapper::toDomain).toList();
	}

	@Override
	public List<Order> findAll() {
		return orderJpaRepository.findAll().stream().map(mapper::toDomain).toList();
	}
}
