package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.application.user.dto.UserOrderResponseDTO;
import kr.hhplus.be.server.config.swagger.SwaggerErrorExample;
import kr.hhplus.be.server.application.user.dto.UserCouponResponseDTO;
import kr.hhplus.be.server.application.user.dto.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import static kr.hhplus.be.server.config.swagger.ErrorCode.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "유저", description = "유저 관련 API")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;

	@GetMapping("/{userId}/point")
	@Operation(description = "유저 보유 포인트 조회")
	@SwaggerErrorExample({
		NOT_EXIST_USER
	})
	public ResponseEntity<ResponseApi<UserResponseDTO>> getUserPoint(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId
	) {
		return ResponseEntity.ok(ResponseApi.of(userService.getUserPoint(userId)));
	}

	@GetMapping("/{userId}/coupon")
	@Operation(description = "유저 보유 쿠폰 조회")
	@SwaggerErrorExample({
		NOT_EXIST_USER
	})
	public ResponseEntity<ResponseApi<UserCouponResponseDTO>> getUserCoupon(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId
	) {
		return ResponseEntity.ok(ResponseApi.of(userService.getUserCoupon(userId)));
	}

	@PostMapping("/{userId}/coupon")
	@Operation(description = "유저 쿠폰 발급")
	@SwaggerErrorExample({
		COUPON_SOLD_OUT,
		NOT_EXIST_USER,
		DUPLICATE_COUPON_CLAIM
	})
	public ResponseEntity<ResponseApi<UserCouponResponseDTO>> issueCoupon(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId
	) {
		return ResponseEntity.ok(ResponseApi.of(userService.issueCoupon(userId)));
	}

	@PostMapping("/{userId}/point")
	@SwaggerErrorExample({
		NOT_EXIST_USER,
		INVALID_CHARGE_AMOUNT
	})
	@Operation(description = "유저 포인트 충전")
	public ResponseEntity<ResponseApi<UserResponseDTO>> chargePoint(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId,
		@Parameter(description = "포인트 충전량", required = true)
		@RequestParam Integer price
	) {
		return ResponseEntity.ok(ResponseApi.of(userService.chargePoint(userId,new BigDecimal(price))));
	}
	@PostMapping("/{userId}/put")
	@SwaggerErrorExample({
		NOT_EXIST_USER,
		INSUFFICIENT_POINTS
	})
	@Operation(description = "유저 포인트 사용")
	public ResponseEntity<ResponseApi<UserResponseDTO>> usePoint(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId,
		@Parameter(description = "포인트 사용량", required = true)
		@RequestParam Integer price
	) {
		return ResponseEntity.ok(ResponseApi.of(userService.usePoint(userId,new BigDecimal(price))));
	}
	@GetMapping("/{userId}/orders")
	@Operation(description = "사용자 주문 목록 조회")
	public ResponseEntity<ResponseApi<UserOrderResponseDTO>> getOrders(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId
	){
		return ResponseEntity.ok(ResponseApi.of(userService.getOrders(userId)));
	}
}
