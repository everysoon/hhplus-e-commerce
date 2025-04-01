package kr.hhplus.be.server.controller;

import static kr.hhplus.be.server.config.swagger.ErrorCode.COUPON_SOLD_OUT;
import static kr.hhplus.be.server.config.swagger.ErrorCode.INSUFFICIENT_POINTS;
import static kr.hhplus.be.server.config.swagger.ErrorCode.LOCK_ACQUISITION_FAIL;
import static kr.hhplus.be.server.config.swagger.ErrorCode.NOT_EXIST_COUPON;
import static kr.hhplus.be.server.config.swagger.ErrorCode.NOT_EXIST_PRODUCT;
import static kr.hhplus.be.server.config.swagger.ErrorCode.NOT_EXIST_USER;
import static kr.hhplus.be.server.config.swagger.ErrorCode.OUT_OF_STOCK;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.swagger.SwaggerErrorExample;
import kr.hhplus.be.server.config.swagger.SwaggerSuccessExample;
import kr.hhplus.be.server.dto.order.OrderRequestDTO;
import kr.hhplus.be.server.dto.order.OrderResponseDTO;
import kr.hhplus.be.server.dto.user.UserCouponResponseDTO;
import kr.hhplus.be.server.service.MockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
@Tag(name="주문",description = "주문 관련 API")
@RequiredArgsConstructor
public class OrderController {
    private final MockService mockService;

    @PostMapping
    @SwaggerSuccessExample(responseType =  OrderResponseDTO.class)
    @SwaggerErrorExample({
        NOT_EXIST_COUPON,
        NOT_EXIST_PRODUCT,
        NOT_EXIST_USER,
        OUT_OF_STOCK,
        INSUFFICIENT_POINTS,
        LOCK_ACQUISITION_FAIL
    })
    public ResponseEntity<ResponseApi<OrderResponseDTO>> getUserPoint(
       @Valid @RequestBody OrderRequestDTO dto
    ) {
        return ResponseEntity.ok(mockService.order(dto));
    }
}
