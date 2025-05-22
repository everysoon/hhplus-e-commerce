package kr.hhplus.be.server.infra.coupon;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.domain.coupon.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CouponJpaRepository extends JpaRepository<Coupon, String> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT c FROM Coupon c WHERE c.id = :id")
	Optional<Coupon> findByIdWithLock(@Param("id") String id);

	List<Coupon> findAllByIdIn(List<String> ids);

	@Query("SELECT c FROM Coupon c WHERE c.expiredAt < :now ")
	List<Coupon> findExpiredAll(@Param("now") LocalDateTime now);

	@Query("SELECT c FROM Coupon c WHERE c.expiredAt > :now ")
	List<Coupon> findNotExpiredAll(@Param("now") LocalDateTime now);
}
