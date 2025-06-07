package kr.hhplus.be.server.infra.product;

import kr.hhplus.be.server.domain.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface ProductJpaRepository extends JpaRepository<Product, Long> , JpaSpecificationExecutor<Product> {
	@Query("""
		SELECT p FROM Product p
		JOIN OrderItem oi ON p.id = oi.productId
		WHERE oi.order.orderedAt BETWEEN :startDate AND :endDate
		GROUP BY p
		ORDER BY SUM(oi.quantity) DESC
		"""
	)
	List<Product> findPopularAll(@Param("startDate")LocalDateTime startDate, @Param("endDate")LocalDateTime endDate, Pageable pageable);

	List<Product> findByIdIn(List<Long> productIds);
}
