package kr.hhplus.be.server.domain.point.repository;

import java.util.Optional;
import kr.hhplus.be.server.domain.point.Point;

public interface PointRepository {
	Optional<Point> findByUserId(Long userId);
	Point save(Point point);
	Point update(Point point);
}
