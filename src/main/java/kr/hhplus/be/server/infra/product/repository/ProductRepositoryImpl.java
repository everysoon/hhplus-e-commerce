package kr.hhplus.be.server.infra.product.repository;

import kr.hhplus.be.server.config.jpa.InMemoryRepository;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.infra.product.entity.ProductEntity;
import org.springframework.stereotype.Repository;

@Repository
public class ProductRepositoryImpl extends InMemoryRepository<Long, ProductEntity> implements
	ProductRepository {

}
