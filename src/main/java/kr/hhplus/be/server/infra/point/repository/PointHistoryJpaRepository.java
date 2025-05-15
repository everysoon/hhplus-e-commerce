package kr.hhplus.be.server.infra.point.repository;

import java.util.List;
import kr.hhplus.be.server.infra.point.entity.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistoryEntity, Long> {
	List<PointHistoryEntity> findByUserId(Long userId);
}
