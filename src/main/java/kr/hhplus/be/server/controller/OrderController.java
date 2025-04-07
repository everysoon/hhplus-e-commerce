package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.order.facade.OrderFacade;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.config.swagger.SwaggerErrorExample;
import kr.hhplus.be.server.application.order.dto.OrderRequestDTO;
import kr.hhplus.be.server.application.order.dto.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
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
	private final OrderFacade orderFacade;

	@PostMapping
    @SwaggerErrorExample({
        NOT_EXIST_COUPON,
		INVALID_COUPON,
        NOT_EXIST_PRODUCT,
        NOT_EXIST_USER,
        OUT_OF_STOCK,
		INVALID_USER_COUPON,
        INSUFFICIENT_POINTS,
        LOCK_ACQUISITION_FAIL,
		NOT_EXIST_ORDER_ITEM
    })
	@Operation(description = "상품 주문")
    public ResponseEntity<ResponseApi<OrderResponseDTO>> order(
       @Valid @RequestBody OrderRequestDTO dto
    ) {
        return ResponseEntity.ok(ResponseApi.of(orderFacade.order(dto)));
    }

}
