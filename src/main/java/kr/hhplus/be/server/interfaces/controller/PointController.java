package kr.hhplus.be.server.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.interfaces.dto.PointDTO;
import kr.hhplus.be.server.interfaces.dto.UserDTO;
import kr.hhplus.be.server.support.config.swagger.SwaggerErrorExample;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.*;

@RestController
@RequestMapping("/api/points")
@Tag(name="포인트",description = "포인트 충전/사용API")
@RequiredArgsConstructor
public class PointController {

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
		 PointCommand.Detail detail = pointService.charge(PointCommand.Charge.of(userId,price));
		return ResponseEntity.ok(ResponseApi.of(PointDTO.UserPointResponse.from(detail)));
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
		PointCommand.Detail detail = pointService.use(PointCommand.Use.of(userId, price));
		return ResponseEntity.ok(ResponseApi.of(PointDTO.UserPointResponse.from(detail)));
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
