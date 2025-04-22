package kr.hhplus.be.server.integration.concurrency;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import kr.hhplus.be.server.utils.UserTestFixture;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PointConcurrencyTest extends BaseIntegrationTest {
	@Autowired
	private PointService pointService;
	@Autowired
	private PointRepository pointRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private ConcurrencyTestHelper concurrencyTestHelper;

	private Long userId = 1L;
	private final int threadCount = 5;

	@BeforeEach
	void setUp() {
		userRepository.save(UserTestFixture.createUser(userId));
		Point point = new Point(null, userId, BigDecimal.valueOf(10000));
		pointRepository.save(point);
	}

	@Test
	void 동시성_포인트_사용_테스트() throws InterruptedException {
		concurrencyTestHelper.run(threadCount, index -> {
			PointCommand.Use command = PointCommand.Use.of(userId, BigDecimal.valueOf(1000));
			pointService.use(command);
		});

		Point userPoint = pointService.getUserPoint(userId);
		assertEquals(5000, userPoint.getBalance().intValue());
	}
	@Test
	void 동시성_포인트_충전_테스트() throws InterruptedException {
		concurrencyTestHelper.run(threadCount, index -> {
			PointCommand.Charge command = PointCommand.Charge.of(userId, BigDecimal.valueOf(1000));
			pointService.charge(command);
		});

		Point userPoint = pointService.getUserPoint(userId);
		assertEquals(15000, userPoint.getBalance().intValue());
	}
}
