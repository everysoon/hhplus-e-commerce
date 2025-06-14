package kr.hhplus.be.server.domain.point;

import java.math.BigDecimal;

import java.util.Optional;

public interface PointRepository {
	Optional<Point> findByUserId(Long userId);
	Optional<Point> findByUserIdWithLock(Long userId);
	Point use(Long userId, BigDecimal amount);

	Point charge(Long userId, BigDecimal amount);
	Point save(Point point);
}
