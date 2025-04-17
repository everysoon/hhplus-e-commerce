package kr.hhplus.be.server.infra.point.repository;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.user.repository.PointHistoryRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class PointHistoryRepositoryImpl implements PointHistoryRepository {

	@Override
	public PointHistory save(PointHistory pointHistory) {
		return null;
	}

	@Override
	public List<PointHistory> findByUserId(Long userId) {
		return List.of();
	}
}
