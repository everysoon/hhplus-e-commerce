package kr.hhplus.be.server.interfaces.controller;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.COUPON_SOLD_OUT;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.DUPLICATE_COUPON_CLAIM;
import static kr.hhplus.be.server.support.config.swagger.ErrorCode.NOT_EXIST_USER;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.coupon.CouponFacade;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.application.coupon.UserCouponDetailResult;
import kr.hhplus.be.server.interfaces.dto.UserCouponDTO;
import kr.hhplus.be.server.interfaces.dto.UserCouponDTO.CouponDetailResponse;
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
@RequestMapping("/api/coupons")
@Tag(name = "쿠폰", description = "쿠폰 발급 및 조회 API")
@RequiredArgsConstructor
public class CouponController {

	private final CouponFacade couponFacade;

	@GetMapping("/{userId}")
	@Operation(description = "유저 보유 쿠폰 조회")
	@SwaggerErrorExample({
		NOT_EXIST_USER
	})
	public ResponseEntity<ResponseApi<List<CouponDetailResponse>>> getUserCoupons(
		@Parameter(description = "유저 ID", required = true)
		@PathVariable Long userId
	) {
		List<UserCouponDetailResult> result = couponFacade.getUserCoupons(userId);
		List<UserCouponDTO.CouponDetailResponse> response = result.stream()
			.map(UserCouponDTO.CouponDetailResponse::from).toList();
		return ResponseEntity.ok(ResponseApi.of(response));
	}

	@PostMapping("/{userId}")
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
		UserCouponDetailResult result = couponFacade.issueCoupon(command);
		return ResponseEntity.ok(ResponseApi.of(UserCouponDTO.CouponDetailResponse.from(result)));
	}
}
