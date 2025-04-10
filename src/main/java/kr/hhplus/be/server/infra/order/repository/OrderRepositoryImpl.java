package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.config.jpa.InMemoryRepository;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderRepositoryImpl extends InMemoryRepository<Long, OrderEntity> implements
	OrderRepository {

}
