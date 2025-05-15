package kr.hhplus.be.server.infra.coupon.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CouponJpaRepository extends JpaRepository<CouponEntity, String> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM CouponEntity c WHERE c.id = :id")
	Optional<CouponEntity> findByIdWithLock(@Param("id") String id);
	List<CouponEntity> findAllByIdIn(List<String> ids);
}
