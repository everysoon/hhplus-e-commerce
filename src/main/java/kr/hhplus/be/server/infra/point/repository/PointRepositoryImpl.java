package kr.hhplus.be.server.infra.point.repository;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.infra.point.entity.PointEntity;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
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
	public Point use(Long userId, BigDecimal amount) {
		PointEntity entity = pointJpaRepository.findByUserIdWithLock(userId).orElseThrow(()->new CustomException(
			ErrorCode.INSUFFICIENT_POINTS));
		Point domain = entity.toDomain();
		domain.use(amount);
		return pointJpaRepository.save(PointEntity.from(domain)).toDomain();
	}

	@Override
	public Point charge(Long userId, BigDecimal amount) {
		PointEntity entity = pointJpaRepository.findByUserIdWithLock(userId).orElseGet(() ->
			pointJpaRepository.save(PointEntity.create(userId))
		);
		Point domain = entity.toDomain();
		domain.charge(amount);
		return pointJpaRepository.save(PointEntity.from(domain)).toDomain();
	}

	@Override
	public Point save(Point point) {
		return pointJpaRepository.save(PointEntity.from(point)).toDomain();
	}
}
