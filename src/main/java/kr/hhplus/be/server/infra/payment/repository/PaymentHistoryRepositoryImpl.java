package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.config.jpa.InMemoryRepository;
import kr.hhplus.be.server.infra.payment.entity.PaymentHistoryEntity;
import kr.hhplus.be.server.domain.payment.repository.PaymentHistoryRepository;

public class PaymentHistoryRepositoryImpl extends InMemoryRepository<Long, PaymentHistoryEntity> implements
	PaymentHistoryRepository {

}
