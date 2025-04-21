package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointHistory;
import kr.hhplus.be.server.domain.point.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.infra.point.entity.PointStatus;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class PointServiceTest extends BaseIntegrationTest {
	@Autowired
	private PointRepository pointRepository;
	@Autowired
	private PointHistoryRepository pointHistoryRepository;

	private PointService pointService;


	@BeforeAll
	void setup() {
		pointService = new PointService(pointRepository, pointHistoryRepository);
	}
	@Test
	void 유저_포인트_충전시_포인트기록을_리턴한다(){
		PointCommand.Charge command = PointCommand.Charge.of(1L, BigDecimal.valueOf(1000));

		PointHistory history = pointService.charge(command);
		assertAll("포인트 기록 상세 확인",
			()->assertThat(history.getStatus()).isEqualTo(PointStatus.CHARGED),
			()->assertThat(history.getPrice()).isEqualTo(BigDecimal.valueOf(1000)),
			()->assertThat(history.getUserId()).isEqualTo(1L)
		);
	}
	@Test
	void 유저_포인트_충전시_충전량이_0보다_작을경우_Throws_INVALID_CHARGE_AMOUNT(){
		PointCommand.Charge command = PointCommand.Charge.of(1L, BigDecimal.valueOf(0));
		CustomException customException = assertThrows(CustomException.class, () -> {
			pointService.charge(command);
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INVALID_CHARGE_AMOUNT);
	}

	@Test
	void 유저_포인트_사용시_포인트가_충분하다면_정상으로_감소된다(){
		PointCommand.Use command = PointCommand.Use.of(1L, BigDecimal.valueOf(100));
		PointHistory history = pointService.use(command);
		assertAll("포인트 기록 상세 확인",
			()->assertThat(history.getStatus()).isEqualTo(PointStatus.USED),
			()->assertThat(history.getPrice()).isEqualTo(BigDecimal.valueOf(100)),
			()->assertThat(history.getUserId()).isEqualTo(1L)
		);
	}
	@Test
	void 유저_포인트_사용시_포인트_충분하지않다면_Throws_INSUFFICIENT_POINTS(){
		PointCommand.Use command = PointCommand.Use.of(1L, BigDecimal.valueOf(10000));
		CustomException customException = assertThrows(CustomException.class, () -> {
			pointService.use(command);
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INSUFFICIENT_POINTS);
	}

	@Test
	void 유저_포인트_조회시_유저_포인트가_조회된다(){
		Point userPoint = pointService.getUserPoint(1L);
		assertThat(userPoint.getBalance().toString()).isEqualTo("700.00");
	}
	@Test
	void 유저_포인트_조회시_유효하지않은_유저ID를_넣으면_NOT_EXIST_POINT_BY_USER_ID(){
		CustomException customException = assertThrows(CustomException.class, () -> {
			pointService.getUserPoint(0L);
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.NOT_EXIST_POINT_BY_USER_ID);
	}
	@Test
	void 주문취소시_포인트가_정상_복원된다(){
		PointCommand.Refund command = PointCommand.Refund.of(1L, BigDecimal.valueOf(100));
		Point point = pointService.refund(command);
		assertThat(point.getBalance().toString()).isEqualTo("800.00");
	}
}
