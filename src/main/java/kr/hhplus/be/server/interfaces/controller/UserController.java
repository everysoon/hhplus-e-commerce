package kr.hhplus.be.server.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.interfaces.dto.UserCouponDTO;
import kr.hhplus.be.server.interfaces.dto.UserDTO;
import kr.hhplus.be.server.support.config.swagger.SwaggerErrorExample;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "유저", description = "유저 관련 API")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserFacade userFacade;

	@GetMapping("/{userId}/point")
	@Operation(description = "유저 보유 포인트 조회")
	@SwaggerErrorExample({
		NOT_EXIST_USER
	})
	public ResponseEntity<ResponseApi<UserDTO.UserResponse>> getUserPoint(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId
	) {
		User user = userService.getUserPoint(userId);
		return ResponseEntity.ok(ResponseApi.of(UserDTO.UserResponse.from(user)));
	}

	@GetMapping("/{userId}/coupon")
	@Operation(description = "유저 보유 쿠폰 조회")
	@SwaggerErrorExample({
		NOT_EXIST_USER
	})
	public ResponseEntity<ResponseApi<UserCouponDTO.Response>> getUserCoupon(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId
	) {
		List<UserCoupon> userCoupons = userFacade.getUserCoupons(userId);
		UserCouponDTO.Response response = UserCouponDTO.Response.of(userId,userCoupons);
		return ResponseEntity.ok(ResponseApi.of(response));
	}

	@PostMapping("/{userId}/coupon")
	@Operation(description = "유저 쿠폰 발급")
	@SwaggerErrorExample({
		COUPON_SOLD_OUT,
		NOT_EXIST_USER,
		DUPLICATE_COUPON_CLAIM
	})
	public ResponseEntity<ResponseApi<UserCouponDTO.Response>> issueCoupon(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId
	) {
		return ResponseEntity.ok(ResponseApi.of(userFacade.issueCoupon(userId)));
	}

	@PostMapping("/{userId}/point")
	@SwaggerErrorExample({
		NOT_EXIST_USER,
		INVALID_CHARGE_AMOUNT
	})
	@Operation(description = "유저 포인트 충전")
	public ResponseEntity<ResponseApi<UserDTO.UserResponse>> chargePoint(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId,
		@Parameter(description = "포인트 충전량", required = true)
		@RequestParam Integer price
	) {
		User user = userService.chargePoint(userId, new BigDecimal(price));
		return ResponseEntity.ok(ResponseApi.of(UserDTO.UserResponse.from(user)));
	}
	@PostMapping("/{userId}/put")
	@SwaggerErrorExample({
		NOT_EXIST_USER,
		INSUFFICIENT_POINTS
	})
	@Operation(description = "유저 포인트 사용")
	public ResponseEntity<ResponseApi<UserDTO.UserResponse>> usePoint(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId,
		@Parameter(description = "포인트 사용량", required = true)
		@RequestParam Integer price
	) {
		User user = userService.usePoint(userId, new BigDecimal(price));
		return ResponseEntity.ok(ResponseApi.of(UserDTO.UserResponse.from(user)));
	}
//	@GetMapping("/{userId}/orders")
//	@Operation(description = "사용자 주문 목록 조회")
//	public ResponseEntity<ResponseApi<>> getOrders(
//		@Parameter(description = "유저 ID", required = true)
//		@PathVariable Long userId
//	){
//		OrderDetail orders = userFacade.getOrders(userId);
//		// dto
//		return ResponseEntity.ok(ResponseApi.of(null));
//	}
}
