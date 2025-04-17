package kr.hhplus.be.server.infra.point.repository;

import kr.hhplus.be.server.infra.point.entity.PointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PointJpaRepository extends JpaRepository<PointEntity, Long> {
	Optional<PointEntity> findByUserId(Long userId);
}
