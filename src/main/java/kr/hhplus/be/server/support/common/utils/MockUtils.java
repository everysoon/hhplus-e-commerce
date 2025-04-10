package kr.hhplus.be.server.support.common.utils;

import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.infra.product.entity.Category;
import kr.hhplus.be.server.domain.product.ProductStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.infra.coupon.entity.CouponEntity;
import kr.hhplus.be.server.infra.order.entity.OrderEntity;
import kr.hhplus.be.server.infra.order.entity.OrderItemEntity;
import kr.hhplus.be.server.infra.payment.entity.PaymentEntity;
import kr.hhplus.be.server.infra.product.entity.ProductEntity;
import kr.hhplus.be.server.infra.user.entity.UserCouponEntity;
import kr.hhplus.be.server.infra.user.entity.UserEntity;

public class MockUtils {
	public static Long userId = 1L;
	public static List<CouponEntity> createCouponList(OrderEntity orderEntity) {
		List<CouponEntity> couponEntities = new ArrayList<>();
		couponEntities.add(createCoupon(orderEntity));
		return couponEntities;
	}
	public static List<CouponEntity> createCouponList(List<UUID> couponIds, OrderEntity orderEntity) {

		return couponIds.stream().map(u->createCoupon(u, orderEntity)).toList();
	}

	public static OrderEntity createOrder(PaymentEntity paymentEntity) {
		return OrderEntity.builder()
			.id(1L)
			.paymentEntity(paymentEntity)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static PaymentEntity createDefaultPayment() {
		return PaymentEntity.builder()
			.paymentMethod(PaymentMethod.POINTS)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static OrderItemEntity createOrderItem(ProductEntity productEntity, Integer quantity, OrderEntity orderEntity) {
		return OrderItemEntity.builder()
			.productEntity(productEntity)
			.orderEntity(orderEntity)
			.quantity(quantity)
			.build();
	}

	public static UserCouponEntity createUserCoupon(UserEntity userEntity, CouponEntity couponEntity, CouponStatus status) {

		return UserCouponEntity.builder()
			.couponEntity(couponEntity)
			.id(1L)
			.userEntity(userEntity)
			.status(status)
			.remainingStock(5)
			.issuedAt(LocalDateTime.now())
			.build();
	}

	public static CouponEntity createExpiredCoupon(UUID uuid) {
		return CouponEntity.builder()
			.id(uuid)
			.orderEntity(null)
			.discount(new BigDecimal(1000))
			.type(CouponType.FIXED)
			.description("TEST COUPON")
			.expiredAt(LocalDateTime.now().minusDays(7L))
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static CouponEntity createCoupon(OrderEntity orderEntity) {
		return CouponEntity.builder()
			.id(UUID.randomUUID())
			.orderEntity(orderEntity)
			.discount(new BigDecimal(1000))
			.type(CouponType.FIXED)
			.description("TEST COUPON")
			.expiredAt(LocalDateTime.now().plusDays(7L))
			.createdAt(LocalDateTime.now())
			.build();
	}
	public static CouponEntity createCoupon(UUID uuid, OrderEntity orderEntity) {
		return CouponEntity.builder()
			.id(uuid)
			.orderEntity(orderEntity)
			.discount(new BigDecimal(1000))
			.type(CouponType.FIXED)
			.description("TEST COUPON")
			.expiredAt(LocalDateTime.now().plusDays(7L))
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static UserEntity createUser(Long userId) {
		return UserEntity.builder()
			.id(userId)
			.point(userId == 2 ? convertToBigDecimal(10000):convertToBigDecimal(0))
			.name("minsoon")
			.email("soonforjoy@gmail.com")
			.address("Guro,Seoul")
			.password("$2a$12$NooMM5e1WBiD8uYqRkblTuNN0iesou/beJ/EeuTofsUmzjhuZ6NgK")
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static ProductEntity createProduct(Long productId, Integer stock) {
		return ProductEntity.builder()
			.id(productId)
			.stock(stock)
			.price(convertToBigDecimal(5000))
			.productName("다이소 장난감 " + productId)
			.category(Category.BABY)
			.description("유아용 다이소 자동차 장난감 " + productId)
			.status(ProductStatus.AVAILABLE)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static BigDecimal convertToBigDecimal(Integer number) {
		return BigDecimal.valueOf(number);
	}
}
