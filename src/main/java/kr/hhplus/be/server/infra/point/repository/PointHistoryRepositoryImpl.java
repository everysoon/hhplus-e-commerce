package kr.hhplus.be.server.infra.point.repository;

import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.infra.point.entity.PointHistoryEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class PointHistoryRepositoryImpl implements PointHistoryRepository {
	private final PointHistoryJpaRepository pointHistoryJpaRepository;
	@Override
	public PointHistory save(PointHistory pointHistory) {
		return pointHistoryJpaRepository.save(PointHistoryEntity.from(pointHistory)).toDomain();
	}

	@Override
	public List<PointHistory> findByUserId(Long userId) {
		return pointHistoryJpaRepository.findByUserId(userId).stream().map(PointHistoryEntity::toDomain).toList();
	}
}
