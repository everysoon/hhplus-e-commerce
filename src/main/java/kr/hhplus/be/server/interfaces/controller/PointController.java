package kr.hhplus.be.server.interfaces.controller;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.INSUFFICIENT_POINTS;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.INVALID_CHARGE_AMOUNT;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.NOT_EXIST_USER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.application.point.UpdatePointCriteria;
import kr.hhplus.be.server.application.point.UserPointResult;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.interfaces.dto.PointDTO;
import kr.hhplus.be.server.interfaces.dto.UserDTO;
import kr.hhplus.be.server.support.config.swagger.SwaggerErrorExample;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/points")
@Tag(name="포인트",description = "포인트 충전/사용API")
@RequiredArgsConstructor
public class PointController {

	private final PointFacade pointFacade;
	private final PointService pointService;

	@PostMapping("/{userId}")
	@SwaggerErrorExample({
		NOT_EXIST_USER,
		INVALID_CHARGE_AMOUNT
	})
	@Operation(description = "유저 포인트 충전")
	public ResponseEntity<ResponseApi<PointDTO.UserPointResponse>> chargePoint(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId,
		@Parameter(description = "포인트 충전량", required = true)
		@RequestParam BigDecimal price
	) {
		UserPointResult result = pointFacade.charge(UpdatePointCriteria.Charge.of(userId,price));
		return ResponseEntity.ok(ResponseApi.of(PointDTO.UserPointResponse.from(result)));
	}
	@PutMapping("/{userId}")
	@SwaggerErrorExample({
		NOT_EXIST_USER,
		INSUFFICIENT_POINTS
	})
	@Operation(description = "유저 포인트 사용")
	public ResponseEntity<ResponseApi<PointDTO.UserPointResponse>> usePoint(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId,
		@Parameter(description = "포인트 사용량", required = true)
		@RequestParam BigDecimal price
	) {
		UserPointResult result =pointFacade.use(UpdatePointCriteria.Use.of(userId, price));
		return ResponseEntity.ok(ResponseApi.of(PointDTO.UserPointResponse.from(result)));
	}
	@GetMapping("/{userId}")
	@Operation(description = "유저 보유 포인트 조회")
	@SwaggerErrorExample({
		NOT_EXIST_USER
	})
	public ResponseEntity<ResponseApi<UserDTO.UserResponse>> getUserPoint(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId
	) {
		Point point = pointService.getUserPoint(userId);
		return ResponseEntity.ok(ResponseApi.of(UserDTO.UserResponse.from(point)));
	}
}
