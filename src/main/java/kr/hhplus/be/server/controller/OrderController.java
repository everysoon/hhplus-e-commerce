package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.swagger.SwaggerErrorExample;
import kr.hhplus.be.server.config.swagger.SwaggerSuccessExample;
import kr.hhplus.be.server.dto.order.OrderRequestDTO;
import kr.hhplus.be.server.dto.order.OrderResponseDTO;
import kr.hhplus.be.server.service.MockOrderService;
import kr.hhplus.be.server.service.MockService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.hhplus.be.server.config.swagger.ErrorCode.*;

@RestController
@RequestMapping("/api/orders")
@Tag(name="주문",description = "주문 관련 API")
@RequiredArgsConstructor
public class OrderController {
    private final MockOrderService mockService;

    @PostMapping
    @SwaggerSuccessExample(responseType =  OrderResponseDTO.class)
    @SwaggerErrorExample({
        NOT_EXIST_COUPON, // 0
		INVALID_COUPON, // 0
        NOT_EXIST_PRODUCT, // 0
        NOT_EXIST_USER, // 0
        OUT_OF_STOCK, // 0
        INSUFFICIENT_POINTS, // 0
        LOCK_ACQUISITION_FAIL,
		NOT_EXIST_ORDER_ITEM // 0
    })
    public ResponseEntity<ResponseApi<OrderResponseDTO>> order(
       @Valid @RequestBody OrderRequestDTO dto
    ) {
        return ResponseEntity.ok(mockService.order(dto));
    }
}
