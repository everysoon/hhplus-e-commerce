package kr.hhplus.be.server.infra.point;

import kr.hhplus.be.server.domain.point.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.PointHistory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {
	private final PointHistoryJpaRepository pointHistoryJpaRepository;
	@Override
	public PointHistory save(PointHistory pointHistory) {
		return pointHistoryJpaRepository.save(pointHistory);
	}

	@Override
	public List<PointHistory> findByUserId(Long userId) {
		return pointHistoryJpaRepository.findByUserId(userId);
	}
}
