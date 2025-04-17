package kr.hhplus.be.server.infra.user.repository;

import kr.hhplus.be.server.infra.point.entity.PointHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistoryEntity, Long> {
}
