package kr.hhplus.be.server.infra.product.repository;

import kr.hhplus.be.server.infra.product.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> {
}
