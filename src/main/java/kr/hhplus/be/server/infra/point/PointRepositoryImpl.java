package kr.hhplus.be.server.infra.point;

import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class PointRepositoryImpl implements PointRepository {
	private final PointJpaRepository pointJpaRepository;

	@Override
	public Optional<Point> findByUserId(Long userId) {
		return pointJpaRepository.findByUserId(userId);
	}

	@Override
	public Optional<Point> findByUserIdWithLock(Long userId) {
		return pointJpaRepository.findByUserIdWithLock(userId);
	}

	@Override
	public Point use(Long userId, BigDecimal amount) {
		Point point = pointJpaRepository.findByUserId(userId).orElseThrow(()->new CustomException(
			ErrorCode.INSUFFICIENT_POINTS));
		point.use(amount);
		return pointJpaRepository.save(point);
	}

	@Override
	public Point charge(Long userId, BigDecimal amount) {
		Point point = pointJpaRepository.findByUserId(userId).orElseGet(() ->
			pointJpaRepository.save(new Point(userId))
		);
		point.charge(amount);
		return pointJpaRepository.save(point);
	}

	@Override
	public Point save(Point point) {
		return pointJpaRepository.save(point);
	}
}
