package kr.hhplus.be.server.interfaces.controller;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.COUPON_SOLD_OUT;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.DUPLICATE_COUPON_CLAIM;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.INSUFFICIENT_POINTS;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.INVALID_CHARGE_AMOUNT;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.NOT_EXIST_USER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.application.coupon.IssuedCouponResult;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.interfaces.dto.UserCouponDTO;
import kr.hhplus.be.server.interfaces.dto.UserDTO;
import kr.hhplus.be.server.support.config.swagger.SwaggerErrorExample;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@Tag(name = "유저", description = "유저 관련 API")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	private final UserFacade userFacade;
	private final PointService pointService;

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
	public ResponseEntity<ResponseApi<UserCouponDTO.IssuedResponse>> issueCoupon(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId,
		@Parameter(description = "쿠폰 UUID", required = true)
		@RequestParam UUID couponId
	) {
		IssueCouponCommand command = IssueCouponCommand.of(userId,couponId);
		IssuedCouponResult issuedCouponResult = userFacade.issueCoupon(command);
		return ResponseEntity.ok(ResponseApi.of(UserCouponDTO.IssuedResponse.from(issuedCouponResult)));
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
		User user = pointService.chargePoint(userId, new BigDecimal(price));
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
		User user = pointService.usePoint(userId, new BigDecimal(price));
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
