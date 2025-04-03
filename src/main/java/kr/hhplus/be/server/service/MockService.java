package kr.hhplus.be.server.service;

import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.swagger.ErrorCode;
import kr.hhplus.be.server.dto.order.OrderItemDTO;
import kr.hhplus.be.server.dto.order.OrderRequestDTO;
import kr.hhplus.be.server.dto.order.OrderResponseDTO;
import kr.hhplus.be.server.dto.product.ProductResponseDTO;
import kr.hhplus.be.server.dto.user.UserCouponResponseDTO;
import kr.hhplus.be.server.dto.user.UserResponseDTO;
import kr.hhplus.be.server.entity.*;
import kr.hhplus.be.server.enums.Category;
import kr.hhplus.be.server.enums.CouponStatus;
import kr.hhplus.be.server.enums.CouponType;
import kr.hhplus.be.server.enums.OrderStatus;
import kr.hhplus.be.server.enums.PaymentMethod;
import kr.hhplus.be.server.enums.PaymentStatus;
import kr.hhplus.be.server.enums.ProductStatus;
import kr.hhplus.be.server.exception.CustomException;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static kr.hhplus.be.server.config.swagger.ErrorCode.*;
import static kr.hhplus.be.server.utils.MockUtils.*;

@Service
@Getter
public class MockService {
	static int defaultStock = 5;
	// 이미 발급된 사용자 정보 저장 (In-Memory)
	private static final Set<Long> issuedUsers = ConcurrentHashMap.newKeySet();
	// 남은 쿠폰 개수 (동시성을 고려해 AtomicInteger 사용)
	private static final AtomicInteger remainingCoupons = new AtomicInteger(defaultStock); // 초기 쿠폰 개수 - 10

	// 인기 상품 조회
	public ResponseApi<List<ProductResponseDTO>> findAllPopularProducts() {
		List<Product> products = new ArrayList<>();
		products.add(createProduct(1L, defaultStock));
		products.add(createProduct(2L, defaultStock));
		products.add(createProduct(3L, defaultStock));
		List<ProductResponseDTO> result = products.stream().map(Product::toResponseDTO)
			.collect(Collectors.toList());
		return new ResponseApi<>(result);
	}

	// 상품 조회
	public ResponseApi<ProductResponseDTO> findProductById(Long productId) {
		if (productId < 0) {
			throw new CustomException(NOT_EXIST_PRODUCT);
		}

		ProductResponseDTO result = createProduct(productId, defaultStock).toResponseDTO();
		return new ResponseApi<>(result);
	}

	// 유저 포인트 충전
	public ResponseApi<UserResponseDTO> chargePoint(Long userId, BigDecimal price) {
		if (userId <= 0) {
			throw new CustomException(NOT_EXIST_USER);
		}
		if (price.compareTo(BigDecimal.ZERO) <= 0) {
			throw new CustomException(INVALID_CHARGE_AMOUNT);
		}
		User user = createUser(userId);
		user.charge(price);
		return new ResponseApi<>(user.toResponseDTO());
	}

	// 유저 포인트 조회
	public ResponseApi<UserResponseDTO> getUserPoint(Long userId) {
		if (userId <= 0) {
			throw new CustomException(NOT_EXIST_USER);
		}
		return new ResponseApi<>(createUser(userId).toResponseDTO());
	}

	// 선착순 쿠폰 등록
	public ResponseApi<UserCouponResponseDTO> issueCoupon(Long userId) {
		if (userId <= 0) {
			throw new CustomException(NOT_EXIST_USER);
		}
		// 이미 발급받은 사용자 검증
		if (issuedUsers.contains(userId)) {
			throw new CustomException(DUPLICATE_COUPON_CLAIM);
		}

		// 쿠폰이 소진되었는지 확인
		if (remainingCoupons.get() <= 0) {
			throw new CustomException(COUPON_SOLD_OUT);
		}

		User user = createUser(userId);
		Coupon coupon = createCoupon(null);
		UserCouponResponseDTO result = createUserCoupon(user, coupon,
			CouponStatus.ISSUED).toResponse(coupon.getExpiredAt());
		issuedUsers.add(userId);
		remainingCoupons.decrementAndGet();

		return new ResponseApi<>(result);
	}

	// 유저 보유 쿠폰 조회
	public ResponseApi<UserCouponResponseDTO> getUserCoupon(Long userId) {
		if (userId <= 0) {
			throw new CustomException(NOT_EXIST_USER);
		}
		User user = createUser(userId);
		Coupon coupon = createCoupon(null);
		UserCouponResponseDTO result = createUserCoupon(user, coupon,
			CouponStatus.USED).toResponse(coupon.getExpiredAt());
		return new ResponseApi<>(result);
	}
}
