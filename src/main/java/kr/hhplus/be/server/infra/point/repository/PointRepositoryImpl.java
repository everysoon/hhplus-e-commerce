package kr.hhplus.be.server.infra.point.repository;

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
		return Optional.of(pointJpaRepository.findByUserId(userId)
			.orElseThrow(()->new CustomException(ErrorCode.NOT_EXIST_POINT_BY_USER_ID))
			.toDomain());
	}

	@Override
	public Point save(Point point) {
		return pointJpaRepository.save(PointEntity.from(point)).toDomain();
	}
}
