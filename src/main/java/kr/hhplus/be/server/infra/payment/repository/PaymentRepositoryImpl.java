package kr.hhplus.be.server.infra.payment.repository;

import kr.hhplus.be.server.config.jpa.InMemoryRepository;
import kr.hhplus.be.server.infra.payment.entity.PaymentEntity;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;

public class PaymentRepositoryImpl extends InMemoryRepository<Long, PaymentEntity> implements
	PaymentRepository {

}
