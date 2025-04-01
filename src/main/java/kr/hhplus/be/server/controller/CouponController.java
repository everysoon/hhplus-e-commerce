package kr.hhplus.be.server.controller;

import static kr.hhplus.be.server.config.swagger.ErrorCode.COUPON_SOLD_OUT;
import static kr.hhplus.be.server.config.swagger.ErrorCode.NOT_EXIST_PRODUCT;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.swagger.SwaggerErrorExample;
import kr.hhplus.be.server.config.swagger.SwaggerSuccessExample;
import kr.hhplus.be.server.dto.product.ProductResponseDTO;
import kr.hhplus.be.server.dto.user.UserCouponResponseDTO;
import kr.hhplus.be.server.service.MockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/coupons")
@Tag(name="쿠폰",description = "쿠폰 관련 API")
@RequiredArgsConstructor
public class CouponController {
    private final MockService mockService;

    @PostMapping("/{userId}")
    @SwaggerSuccessExample(responseType =  UserCouponResponseDTO.class)
    @SwaggerErrorExample({
        COUPON_SOLD_OUT
    })
    public ResponseEntity<ResponseApi<UserCouponResponseDTO>> getUserPoint(
        @Parameter(description = "유저 ID", required = true)
        @PathVariable Long userId
    ) {
        return ResponseEntity.ok(mockService.issueCoupon(userId));
    }
}
