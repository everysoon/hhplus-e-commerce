package kr.hhplus.be.server.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.application.coupon.UserCouponDetailResult;
import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.interfaces.dto.UserCouponDTO;
import kr.hhplus.be.server.interfaces.dto.UserDTO;
import kr.hhplus.be.server.support.config.swagger.SwaggerErrorExample;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.*;

@RestController
@RequestMapping("/api/users")
@Tag(name = "유저", description = "유저 관련 API")
@RequiredArgsConstructor
public class UserController {
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
		Point point = userFacade.getUserPoint(userId);
		return ResponseEntity.ok(ResponseApi.of(UserDTO.UserResponse.from(point)));
	}

	@GetMapping("/{userId}/coupon")
	@Operation(description = "유저 보유 쿠폰 조회")
	@SwaggerErrorExample({
		NOT_EXIST_USER
	})
	public ResponseEntity<ResponseApi<List<UserCouponDTO.CouponDetailResponse>>> getUserCoupon(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId
	) {
		List<UserCouponDetailResult> result = userFacade.getUserCoupons(userId);
		List<UserCouponDTO.CouponDetailResponse> response = result.stream().map(UserCouponDTO.CouponDetailResponse::from).toList();
		return ResponseEntity.ok(ResponseApi.of(response));
	}

	@PostMapping("/{userId}/coupon")
	@Operation(description = "유저 쿠폰 발급")
	@SwaggerErrorExample({
		COUPON_SOLD_OUT,
		NOT_EXIST_USER,
		DUPLICATE_COUPON_CLAIM
	})
	public ResponseEntity<ResponseApi<UserCouponDTO.CouponDetailResponse>> issueCoupon(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId,
		@Parameter(description = "쿠폰 UUID", required = true)
		@RequestParam UUID couponId
	) {
		IssueCouponCommand command = IssueCouponCommand.of(userId, couponId);
		UserCouponDetailResult result = userFacade.issueCoupon(command);
		return ResponseEntity.ok(ResponseApi.of(UserCouponDTO.CouponDetailResponse.from(result)));
	}
}
