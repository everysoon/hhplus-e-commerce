package kr.hhplus.be.server.unit.service;

import static kr.hhplus.be.server.utils.TestFixture.toBigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import kr.hhplus.be.server.application.order.service.OrderHistoryService;
import kr.hhplus.be.server.application.order.service.OrderItemService;
import kr.hhplus.be.server.application.user.UserCouponService;
import kr.hhplus.be.server.application.user.UserFacade;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderCoupon;
import kr.hhplus.be.server.domain.order.OrderDetail;
import kr.hhplus.be.server.domain.order.OrderHistory;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.user.UserCoupon;
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
}

