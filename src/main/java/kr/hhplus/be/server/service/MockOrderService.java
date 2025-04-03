package kr.hhplus.be.server.service;

import jakarta.transaction.Transactional;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.dto.order.OrderItemDTO;
import kr.hhplus.be.server.dto.order.OrderProductRequestDTO;
import kr.hhplus.be.server.dto.order.OrderRequestDTO;
import kr.hhplus.be.server.dto.order.OrderResponseDTO;
import kr.hhplus.be.server.entity.*;
import kr.hhplus.be.server.enums.CouponStatus;
import kr.hhplus.be.server.enums.OrderStatus;
import kr.hhplus.be.server.enums.PaymentStatus;
import kr.hhplus.be.server.exception.CustomException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static kr.hhplus.be.server.config.swagger.ErrorCode.*;
import static kr.hhplus.be.server.utils.MockUtils.*;

@Service

public class MockOrderService {
	// 남은 상품 재고
	private static final AtomicInteger remainingProductStock = new AtomicInteger(5);
	private static final Map<Long, List<UUID>> issuedCoupons = new HashMap<>();
	private static final Set<Long> lockedOrders = ConcurrentHashMap.newKeySet();

	public void issuedDefaultCouponAndValidCoupon(OrderRequestDTO dto) {
		// DB를 사용한다 가정
		issuedCoupons.put(2L, List.of(UUID.fromString("b284611e-3518-4857-ab86-dcacf9ceb0a1"), UUID.fromString("921db6fe-f91a-423c-a3ad-7ee219a05a24")));
		if (dto.getCouponId()!= null && !dto.getCouponId().isEmpty()) {
			List<UUID> uuids = issuedCoupons.get(dto.getUserId());
			if(uuids == null || uuids.isEmpty()) {
				throw new CustomException(INVALID_USER_COUPON);
			}
			List<UUID> requestUuid = dto.getCouponId();
			IntStream.range(0, Math.min(uuids.size(), requestUuid.size()))
				.forEach(i -> {
					if(requestUuid.get(i).equals(UUID.fromString("0cf78826-f30f-4121-9476-10f6ac1f6e7f"))){
						throw new CustomException(INVALID_COUPON);
					}
					if (!uuids.get(i).equals(requestUuid.get(i))) {
						throw new CustomException(NOT_EXIST_COUPON);
					}
				});
		}
	}

	public ResponseApi<OrderResponseDTO> order(OrderRequestDTO dto) {

		if (dto.getUserId() <= 0) throw new CustomException(NOT_EXIST_USER);
		if (dto.getProducts().isEmpty()) throw new CustomException(NOT_EXIST_ORDER_ITEM);
		issuedDefaultCouponAndValidCoupon(dto);

		if (!acquireLockForOrder(dto.getUserId())) {
			throw new CustomException(LOCK_ACQUISITION_FAIL);
		}
		try {
			User user = createUser(dto.getUserId());
			List<OrderItem> orderItems = processOrder(dto, user);
			remainingProductStock.decrementAndGet();
			OrderResponseDTO result = buildOrderResponse(user, orderItems);
			return new ResponseApi<>(result);
		} finally {
			releaseLockForOrder(dto.getUserId());
		}
	}

	private List<OrderItem> processOrder(OrderRequestDTO dto, User user) {
		Payment payment = createDefaultPayment();
		Order order = createOrder(payment);

		List<OrderItem> orderItems = createOrderItems(dto.getProducts(), order);
		order.setTotalPrice(calculateTotalPrice(orderItems));

		if (user.getPoint().compareTo(order.getTotalPrice()) < 0) {
			throw new CustomException(INSUFFICIENT_POINTS);
		}

		applyCoupons(dto.getCouponId(),order, user);
		return orderItems;
	}

	private List<OrderItem> createOrderItems(List<OrderProductRequestDTO> products, Order order) {
		return products.stream()
			.map(opr -> {
				if (opr.getProductId() < 0) throw new CustomException(NOT_EXIST_PRODUCT);
				return createProduct(opr.getProductId(), 1);
			})
			.map(p -> {
				if (remainingProductStock.get() < 0) throw new CustomException(OUT_OF_STOCK);
				return createOrderItem(p, 10, order);
			})
			.toList();
	}

	private BigDecimal calculateTotalPrice(List<OrderItem> orderItems) {
		return orderItems.stream()
			.map(OrderItem::getProduct)
			.map(Product::getPrice)
			.reduce(BigDecimal::add)
			.orElse(BigDecimal.ZERO);
	}

	private void applyCoupons(List<UUID> couponIds,Order order, User user) {
		if(couponIds != null && !couponIds.isEmpty()) {
			 createCouponList(couponIds, order).stream()
				.peek(c -> c.calculateDiscount(order.getTotalPrice()))
				.peek(c -> createUserCoupon(user, c, CouponStatus.ISSUED));
		}
	}

	private OrderResponseDTO buildOrderResponse(User user, List<OrderItem> orderItems) {
		Order order = orderItems.stream().map(OrderItem::getOrder).findFirst().orElse(null);

		return OrderResponseDTO.builder()
			.userId(user.getId())
			.orderId(order.getId())
			.paymentMethod(order.getPayment().getPaymentMethod())
			.paymentStatus(PaymentStatus.COMPLETED)
			.totalPrice(order.getTotalPrice())
			.couponDiscountAmount(order.getTotalDiscount())
			.orderInfo(orderItems.stream().map(OrderItem::toDTO).toList())
			.orderedAt(order.getCreatedAt())
			.status(OrderStatus.ORDERED)
			.build();
	}

	private boolean acquireLockForOrder(Long userId) {
		return lockedOrders.add(userId); // 성공적으로 추가되면 true 반환 (락 획득 성공)
	}

	private void releaseLockForOrder(Long userId) {
		lockedOrders.remove(userId); // 주문 처리 후 락 해제
	}
}
