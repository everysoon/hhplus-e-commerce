package kr.hhplus.be.server.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderInfoResult;
import kr.hhplus.be.server.application.order.PlaceOrderResult;
import kr.hhplus.be.server.application.order.RequestOrderCriteria;
import kr.hhplus.be.server.interfaces.dto.OrderDTO;
import kr.hhplus.be.server.support.config.swagger.SwaggerErrorExample;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.*;

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
    public ResponseEntity<ResponseApi<OrderDTO.OrderResponse>> order(
       @Valid @RequestBody OrderDTO.OrderRequest dto
    ) {
		RequestOrderCriteria command = RequestOrderCriteria.from(dto);
		PlaceOrderResult result = orderFacade.placeOrder(command);
		return ResponseEntity.ok(ResponseApi.of(OrderDTO.OrderResponse.of(result)));
    }
	@GetMapping("/{userId}")
	@Operation(description = "유저 주문 목록 조회")
	public ResponseEntity<ResponseApi<OrderDTO.UserOrderResponse>> getOrders(@PathVariable Long userId){
		OrderInfoResult orders = orderFacade.getOrders(userId);
		return ResponseEntity.ok(ResponseApi.of(OrderDTO.UserOrderResponse.of(orders)));
	}

}
