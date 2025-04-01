package kr.hhplus.be.server.repository;

import kr.hhplus.be.server.entity.PointHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
