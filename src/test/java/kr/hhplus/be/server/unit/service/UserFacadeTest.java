package kr.hhplus.be.server.unit.service;

import static kr.hhplus.be.server.utils.TestFixture.toBigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.application.coupon.IssuedCouponResult;
import kr.hhplus.be.server.application.order.service.OrderHistoryService;
import kr.hhplus.be.server.application.order.service.OrderItemService;
import kr.hhplus.be.server.application.user.UserCouponService;
import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCoupon;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserCoupon;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import kr.hhplus.be.server.utils.TestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
class UserFacadeTest {
	@Mock
	private OrderHistoryService orderHistoryService;
	@Mock
	private OrderItemService orderItemService;
	@Mock
	private UserCouponService userCouponService;
	@Mock
	private CouponService couponService;
	@Mock
	private UserService userService;
	@InjectMocks
	private UserFacade userFacade;

	@Test
	void 유저_주문_조회시_히스토리와_아이템_리스트_반환() {
		// given
		Payment payment = mock(Payment.class);
		OrderCoupon orderCoupon = mock(OrderCoupon.class);
		Order order1 = new Order(1L,1L,payment,List.of(orderCoupon), BigDecimal.TEN,toBigDecimal(1000),LocalDateTime.now());
		Order order2 = new Order(2L,1L,payment,List.of(orderCoupon), BigDecimal.TEN,toBigDecimal(1000),LocalDateTime.now());
		OrderHistory history1 = OrderHistory.of(order1, "ORDER 1");
		OrderHistory history2 = OrderHistory.of(order2, "ORDER2");
		List<OrderHistory> histories = List.of(history1, history2);

		OrderItem item1 = new OrderItem(null, TestFixture.createProduct(1L), 1L, 10);
		OrderItem item2 = new OrderItem(null, TestFixture.createProduct(2L), 2L, 10);

		when(orderHistoryService.findByUserId(1L)).thenReturn(histories);
		when(orderItemService.findByOrderId(1L)).thenReturn(List.of(item1));
		when(orderItemService.findByOrderId(2L)).thenReturn(List.of(item2));

		// when
		OrderDetail result = userFacade.getOrders(1L);

		// then
		assertNotNull(result);
		assertEquals(1L, result.getUserId());
		assertEquals(2, result.getOrderItems().size());
		assertEquals(2, result.getOrderHistories().size());

		verify(orderHistoryService).findByUserId(1L);
		verify(orderItemService).findByOrderId(1L);
		verify(orderItemService).findByOrderId(2L);
	}

	@Test
	void 유저의_쿠폰_목록_조회_정상작동() {
		// given
		Long userId = 1L;
		UserCoupon coupon1 = new UserCoupon(userId, null, null, CouponStatus.ISSUED,
			LocalDateTime.now());
		UserCoupon coupon2 = new UserCoupon(userId, null, null, CouponStatus.USED,
			LocalDateTime.now());

		when(userCouponService.findByUserId(userId)).thenReturn(List.of(coupon1, coupon2));

		// when
		List<UserCoupon> result = userFacade.getUserCoupons(userId);

		// then
		assertNotNull(result);
		assertEquals(2, result.size());
		verify(userCouponService).findByUserId(userId);
	}
	@Test
	void 정상적인_쿠폰_발급() {
		// given
		IssueCouponCommand command = IssueCouponCommand.of(1L, UUID.fromString("307b9137-6e63-4dec-9a70-f8fe5c46700f"));
		Coupon coupon = mock(Coupon.class);
		User user = mock(User.class);

		when(user.getId()).thenReturn(1L);
		when(userCouponService.countCouponByUserId(command)).thenReturn(0L); // 중복 발급 아님
		when(couponService.findById(command.couponId())).thenReturn(coupon);
		when(coupon.isOutOfStock()).thenReturn(false); // 품절 아님
		when(coupon.isExpired()).thenReturn(false); // 만료 아님
		when(userService.get(command.userId())).thenReturn(user); // 유저 조회
		when(userCouponService.save(any(UserCoupon.class))).thenReturn(mock(UserCoupon.class)); // 쿠폰 발급

		// when
		IssuedCouponResult result = userFacade.issueCoupon(command);

		// then
		assertNotNull(result);
		verify(userCouponService).save(any(UserCoupon.class)); // 유저 쿠폰 발급
	}
	@Test
	void 쿠폰이_중복_발급_될_경우_예외_발생() {
		// given
		IssueCouponCommand command = IssueCouponCommand.of(1L, UUID.fromString("307b9137-6e63-4dec-9a70-f8fe5c46700f"));

		when(userCouponService.countCouponByUserId(command)).thenReturn(1L); // 이미 쿠폰이 발급됨

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			userFacade.issueCoupon(command);
		});
		assertEquals(ErrorCode.DUPLICATE_COUPON_CLAIM, exception.getErrorCode());
	}

	@Test
	void 쿠폰이_품절일_경우_예외_발생() {
		// given
		IssueCouponCommand command = IssueCouponCommand.of(1L, UUID.fromString("307b9137-6e63-4dec-9a70-f8fe5c46700f"));
		Coupon coupon = mock(Coupon.class);

		when(userCouponService.countCouponByUserId(command)).thenReturn(0L); // 중복 발급 아님
		when(coupon.isOutOfStock()).thenReturn(true); // 쿠폰 품절
		when(couponService.findById(command.couponId())).thenReturn(coupon);

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			userFacade.issueCoupon(command);  // 쿠폰 발급 시도
		});

		// 예외 발생 및 에러 코드 확인
		assertEquals(ErrorCode.COUPON_SOLD_OUT, exception.getErrorCode());
	}


	@Test
	void 쿠폰이_만료일_경우_예외_발생() {
		// given
		IssueCouponCommand command = IssueCouponCommand.of(1L, UUID.fromString("307b9137-6e63-4dec-9a70-f8fe5c46700f"));
		Coupon coupon = mock(Coupon.class);

		when(userCouponService.countCouponByUserId(command)).thenReturn(0L); // 중복 발급 아님
		when(couponService.findById(command.couponId())).thenReturn(coupon);
		when(coupon.isOutOfStock()).thenReturn(false); // 품절 아님
		when(coupon.isExpired()).thenReturn(true); // 품절 아님

		// when & then
		CustomException exception = assertThrows(CustomException.class, () -> {
			userFacade.issueCoupon(command);
		});
		assertEquals(ErrorCode.COUPON_EXPIRED, exception.getErrorCode());
	}


}

