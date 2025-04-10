package kr.hhplus.be.server.infra.order.repository;

import kr.hhplus.be.server.config.jpa.InMemoryRepository;
import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import kr.hhplus.be.server.infra.order.entity.OrderHistoryEntity;
import org.springframework.stereotype.Repository;

@Repository
public class OrderHistoryRepositoryImpl extends InMemoryRepository<Long, OrderHistoryEntity> implements
	OrderHistoryRepository {

}
