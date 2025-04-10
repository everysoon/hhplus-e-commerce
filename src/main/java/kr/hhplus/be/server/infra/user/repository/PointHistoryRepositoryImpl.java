package kr.hhplus.be.server.infra.user.repository;

import java.util.List;
import kr.hhplus.be.server.config.jpa.InMemoryRepository;
import kr.hhplus.be.server.domain.user.PointHistory;
import kr.hhplus.be.server.infra.user.entity.PointHistoryEntity;
import kr.hhplus.be.server.domain.user.repository.PointHistoryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PointHistoryRepositoryImpl extends InMemoryRepository<Long, PointHistoryEntity> implements
	PointHistoryRepository {

	@Override
	public PointHistory save(PointHistory pointHistory) {
		return null;
	}

	@Override
	public List<PointHistory> findByUserId(Long userId) {
		return List.of();
	}
}
