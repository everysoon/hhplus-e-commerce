package kr.hhplus.be.server.infra.point.repository;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.infra.point.entity.PointEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {
	private final PointJpaRepository pointJpaRepository;

	@Override
	public Optional<Point> findByUserId(Long userId) {
		return pointJpaRepository.findByUserId(userId).map(PointEntity::toDomain);
	}

	@Override
	public Optional<Point> findByUserIdWithLock(Long userId) {
		return pointJpaRepository.findByUserIdWithLock(userId)
			.map(PointEntity::toDomain);
	}

	@Override
	public Point save(Point point) {
		return pointJpaRepository.save(PointEntity.from(point)).toDomain();
	}
}
