package kr.hhplus.be.server.integration.concurrency;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import kr.hhplus.be.server.utils.UserTestFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
public class PointConcurrencyTest extends BaseIntegrationTest {
	@Autowired
	private PointService pointService;
	@Autowired
	private PointRepository pointRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ConcurrencyTestHelper concurrencyTestHelper;

	private final int threadCount = 5;

	@Test
	void 동시성_포인트_사용_테스트() throws InterruptedException {
		Long userId = 1L;
		userRepository.save(UserTestFixture.createUser(userId));
		Point point = new Point(userId, BigDecimal.valueOf(10000));
		pointRepository.save(point);

		concurrencyTestHelper.run(threadCount, index -> {
			PointCommand.Use command = PointCommand.Use.of(userId, BigDecimal.valueOf(1000));
			PointCommand.Detail detail = pointService.use(command);
			log.info("detail: {}", detail);
		});
		Point userPoint = pointService.getUserPoint(userId);
		assertEquals(5000, userPoint.getBalance().intValue());
	}

	@Test
	void 동시성_포인트_충전_테스트() throws InterruptedException {
		Long userId = 2L;
		userRepository.save(UserTestFixture.createUser(userId));
		Point point = new Point(userId, BigDecimal.valueOf(10000));
		pointRepository.save(point);

		concurrencyTestHelper.run(threadCount, index -> {
			PointCommand.Charge command = PointCommand.Charge.of(userId, BigDecimal.valueOf(1000));
			PointCommand.Detail detail = pointService.charge(command);
			log.info("detail: {}", detail);
		});

		Point userPoint = pointService.getUserPoint(userId);
		assertEquals(15000, userPoint.getBalance().intValue());
	}
}
