package kr.hhplus.be.server.infra.point.repository;

import java.util.Optional;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import org.springframework.stereotype.Repository;

@Repository
public class PointRepositoryImpl implements PointRepository {

	@Override
	public Optional<Point> findByUserId(Long userId) {
		return Optional.empty();
	}

	@Override
	public Point save(Point point) {
		return null;
	}
}
