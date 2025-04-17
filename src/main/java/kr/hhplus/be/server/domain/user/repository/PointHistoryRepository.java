package kr.hhplus.be.server.domain.user.repository;

import java.util.List;
import kr.hhplus.be.server.domain.point.PointHistory;

public interface PointHistoryRepository {
	PointHistory save(PointHistory pointHistory);
	List<PointHistory> findByUserId(Long userId);
}
