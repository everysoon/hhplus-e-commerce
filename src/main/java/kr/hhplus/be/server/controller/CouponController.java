package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.swagger.SwaggerErrorExample;
import kr.hhplus.be.server.config.swagger.SwaggerSuccessExample;
import kr.hhplus.be.server.dto.user.UserCouponResponseDTO;
import kr.hhplus.be.server.service.MockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.hhplus.be.server.config.swagger.ErrorCode.*;

@RestController
@RequestMapping("/api/coupons")
@Tag(name="쿠폰",description = "쿠폰 관련 API")
@RequiredArgsConstructor
public class CouponController {
    private final MockService mockService;

    @PostMapping("/{userId}")
    @SwaggerSuccessExample(responseType =  UserCouponResponseDTO.class)
    @SwaggerErrorExample({
        COUPON_SOLD_OUT,
		NOT_EXIST_USER,
		DUPLICATE_COUPON_CLAIM
    })
    public ResponseEntity<ResponseApi<UserCouponResponseDTO>> issueCoupon(
        @Parameter(description = "유저 ID", required = true)
        @PathVariable Long userId
    ) {
        return ResponseEntity.ok(mockService.issueCoupon(userId));
    }
}
