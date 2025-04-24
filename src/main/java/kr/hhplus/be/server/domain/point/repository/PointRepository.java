package kr.hhplus.be.server.domain.point.repository;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.point.Point;

import java.util.Optional;

public interface PointRepository {
	Optional<Point> findByUserId(Long userId);
	Optional<Point> findByUserIdWithLock(Long userId);
	void use(Long userId, BigDecimal amount);
	void charge(Long userId, BigDecimal amount);
	Point save(Point point);
}
