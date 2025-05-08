package kr.hhplus.be.server.infra.product.repository;

import kr.hhplus.be.server.infra.product.entity.ProductEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductJpaRepository extends JpaRepository<ProductEntity, Long> , JpaSpecificationExecutor<ProductEntity> {
	@Query("""
		SELECT p FROM ProductEntity p
		JOIN OrderItemEntity oi ON p.id = oi.productId
		WHERE oi.order.orderedAt BETWEEN :startDate AND :endDate
		GROUP BY p
		ORDER BY SUM(oi.quantity) DESC
		"""
	)
	List<ProductEntity> findPopularAll(@Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate, Pageable pageable);

	List<ProductEntity> findByIdIn(List<Long> productIds);
}
