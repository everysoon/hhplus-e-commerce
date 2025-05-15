package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.domain.order.repository.OrderHistoryRepository;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceTest extends BaseIntegrationTest {

	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private OrderHistoryRepository orderHistoryRepository;
	private OrderService orderService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CouponService couponService;

	@BeforeAll
	void setup() {
		orderService = new OrderService(orderRepository, orderHistoryRepository);
	}

//	@Test
//	void 주문_생성이_정상적으로_된다() {
//		Product product = productService.findById(1L);
//
//		List<OrderItem> orderItems = List.of(
//			new OrderItem(1L, product, null, 10, product.getPrice())
//		);
//		UseCouponInfo couponInfo = new UseCouponInfo(1L, null);
//		OrderCommand.Create command = OrderCommand.Create.of(orderItems, couponInfo);
//
//		Order order = orderService.create(command);
//
//		assertThat(order).isNotNull();
//		assertThat(order.getTotalPrice()).isEqualTo(new BigDecimal("2185.00"));
//		assertThat(order.getTotalDiscount().toString()).isEqualTo("0");
//	}
//
//	@Test
//	void 주문_생성시_쿠폰적용이_정상적으로_된다() {
//		Product product = productService.findById(1L);
//		// discount : 1000.00 , FIXED coupon
//		List<Coupon> coupons = List.of(
//			couponService.findCouponById("000479a5-e942-47a0-86f2-3127d6982bf5"));
//
//		UseCouponInfo couponInfo = UseCouponInfo.from(1L, coupons);
//		List<OrderItem> orderItems = List.of(
//			// product unit pridce : 2185.00
//			new OrderItem(1L, product, null, 10, product.getPrice())
//		);
//		OrderCommand.Create command = OrderCommand.Create.of(orderItems, couponInfo);
//
//		Order order = orderService.create(command);
//
//		assertThat(order.getTotalDiscount().toString()).isEqualTo("1000.00");
//		// 2185 - 1000 = 1185
//		assertThat(order.getTotalPrice().toString()).isEqualTo("1185.00");
//	}
//
//	@Test
//	void 주문_생성시_만료된_쿠폰을_적용하면_Throws_EXPIRED_COUPON() {
//		Product product = productService.findById(1L);
//		// discount : 1000.00 , FIXED coupon
//		List<Coupon> coupons = List.of(
//			couponService.findCouponById("000479a5-e942-47a0-86f2-3127d6982bf5"));
//
//		UseCouponInfo couponInfo = UseCouponInfo.from(1L, coupons);
//		List<OrderItem> orderItems = List.of(
//			// product unit pridce : 2185.00
//			new OrderItem(1L, product, null, 10, product.getPrice())
//		);
//		OrderCommand.Create command = OrderCommand.Create.of(orderItems, couponInfo);
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			orderService.create(command);
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.EXPIRED_COUPON);
//	}

}
