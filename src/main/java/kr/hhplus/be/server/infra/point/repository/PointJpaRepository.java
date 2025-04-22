package kr.hhplus.be.server.infra.point.repository;

import jakarta.persistence.LockModeType;
import kr.hhplus.be.server.infra.point.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<PointEntity, Long> {
	@Lock(LockModeType.PESSIMISTIC_WRITE)
	@Query("SELECT p FROM PointEntity p WHERE p.userId = :userId")
	Optional<PointEntity> findByUserIdWithLock(@Param("userId") Long userId);

	Optional<PointEntity> findByUserId(Long userId);
}
