package kr.hhplus.be.server.infra.point.repository;

import java.util.Optional;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.infra.point.entity.PointEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {
	private final PointJpaRepository pointJpaRepository;
	@Override
	public Optional<Point> findByUserId(Long userId) {
		return pointJpaRepository.findByUserId(userId).map(PointEntity::toDomain);

	}

	@Override
	public Point save(Point point) {
		return pointJpaRepository.save(PointEntity.from(point)).toDomain();
	}

	@Override
	public Point update(Point point) {
		return pointJpaRepository.save(PointEntity.update(point)).toDomain();
	}
}
