package kr.hhplus.be.server.infra.point;

import java.util.List;
import kr.hhplus.be.server.domain.point.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryJpaRepository extends JpaRepository<PointHistory, Long> {
	List<PointHistory> findByUserId(Long userId);
}
