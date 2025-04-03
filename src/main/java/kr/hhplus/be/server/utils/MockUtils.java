package kr.hhplus.be.server.utils;

import kr.hhplus.be.server.entity.*;
import kr.hhplus.be.server.enums.*;
import kr.hhplus.be.server.exception.CustomException;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static kr.hhplus.be.server.config.swagger.ErrorCode.INVALID_COUPON;

public class MockUtils {
	public static Long userId = 1L;
	public static List<Coupon> createCouponList(Order order) {
		List<Coupon> coupons = new ArrayList<>();
		coupons.add(createCoupon(order));
		return coupons;
	}

	public static Order createOrder(Payment payment) {
		return Order.builder()
			.id(1L)
			.payment(payment)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static Payment createDefaultPayment() {
		return Payment.builder()
			.paymentMethod(PaymentMethod.POINTS)
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static OrderItem createOrderItem(Product product, Integer quantity, Order order) {
		return OrderItem.builder()
			.product(product)
			.order(order)
			.quantity(quantity)
			.build();
	}

	public static UserCoupon createUserCoupon(User user, Coupon coupon, CouponStatus status) {
		if(coupon.getId() == UUID.fromString("8282")){
			throw new CustomException(INVALID_COUPON);
		}
		return UserCoupon.builder()
			.coupon(coupon)
			.id(1L)
			.user(user)
			.status(status)
			.remainingStock(5)
			.issuedAt(LocalDateTime.now())
			.build();
	}

	public static Coupon createExpiredCoupon(UUID uuid) {
		return Coupon.builder()
			.id(uuid)
			.order(null)
			.discount(new BigDecimal(1000))
			.type(CouponType.FIXED)
			.description("TEST COUPON")
			.expiredAt(LocalDateTime.now().minusDays(7L))
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static Coupon createCoupon(Order order) {
		return Coupon.builder()
			.id(UUID.randomUUID())
			.order(order)
			.discount(new BigDecimal(1000))
			.type(CouponType.FIXED)
			.description("TEST COUPON")
			.expiredAt(LocalDateTime.now().plusDays(7L))
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static User createUser(Long userId) {
		return User.builder()
			.id(userId)
			.point(convertToBigDecimal(0))
			.name("minsoon")
			.email("soonforjoy@gmail.com")
			.address("Guro,Seoul")
			.password("$2a$12$NooMM5e1WBiD8uYqRkblTuNN0iesou/beJ/EeuTofsUmzjhuZ6NgK")
			.createdAt(LocalDateTime.now())
			.build();
	}

	public static Product createProduct(Long productId, Integer stock) {
		return Product.builder()
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
