package kr.hhplus.be.server.unit.service;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.PAYMENT_FAIL;
import static kr.hhplus.be.server.utils.TestFixture.createOrder;
import static kr.hhplus.be.server.utils.TestFixture.createProduct;
import static kr.hhplus.be.server.utils.TestFixture.createUser;
import static kr.hhplus.be.server.utils.TestFixture.toBigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.application.coupon.UseCouponCommand;
import kr.hhplus.be.server.application.order.CreateOrderCommand;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.PlaceOrderResult;
import kr.hhplus.be.server.application.order.RequestOrderCriteria;
import kr.hhplus.be.server.application.order.RequestOrderCriteria.Item;
import kr.hhplus.be.server.application.order.SaveOrderCouponCommand;
import kr.hhplus.be.server.application.order.service.OrderCouponService;
import kr.hhplus.be.server.application.order.service.OrderItemService;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.application.payment.RequestPaymentCommand;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserCouponService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.payment.PaymentStatus;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import kr.hhplus.be.server.utils.TestFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderFacadeTest {

	@Mock
	private OrderService orderService;
	@Mock
	private PaymentService paymentService;
	@Mock
	private ProductService productService;
	@Mock
	private UserCouponService userCouponService;
	@Mock
	private OrderItemService orderItemService;
	@Mock
	private UserService userService;
	@Mock
	private OrderCouponService orderCouponService;

	@InjectMocks
	private OrderFacade orderFacade;

	@Test
	void 주문_정상작동_쿠폰없이() {
		// given
		Payment payment = mock(Payment.class);
		Order order = spy(TestFixture.createOrder(payment));
		RequestOrderCriteria criteria = mock(RequestOrderCriteria.class);

		when(orderService.create(any(CreateOrderCommand.class))).thenReturn(order);
		when(criteria.userId()).thenReturn(1L);
		when(criteria.orderItems()).thenReturn(List.of(new Item(1L, 10))); // 주문 항목
		when(productService.findById(1L)).thenReturn(createProduct(1L));
		when(orderService.create(any())).thenReturn(order);

		// when
		PlaceOrderResult result = orderFacade.order(criteria);

		// then
		assertNotNull(result);
		verify(orderService).create(any(CreateOrderCommand.class));
		verify(paymentService).pay(any(RequestPaymentCommand.class));
		verify(orderService).save(order);
		verify(userService).get(1L);

		// 쿠폰 관련 메서드들이 호출되지만, 쿠폰이 없으므로 그 부분을 검증
		verify(userCouponService, times(0)).use(any(UseCouponCommand.class));
		verify(orderCouponService, times(0)).saveAll(any(SaveOrderCouponCommand.class));

	}

	@Test
	void 유효한_쿠폰이_아닐_경우() {
		// given
		RequestOrderCriteria criteria = mock(RequestOrderCriteria.class);
		when(criteria.couponIds()).thenReturn(List.of(UUID.randomUUID()));
		when(criteria.userId()).thenReturn(1L);

		// when & then
		CustomException customException = assertThrows(CustomException.class, () -> {
			orderFacade.order(criteria);
		});
		assertEquals(ErrorCode.INVALID_COUPON, customException.getErrorCode());
	}

	@Test
	void 유저_포인트_부족시_예외_발생() {
		// given
		Payment payment = mock(Payment.class);
		Order order = spy(TestFixture.createOrder(payment));
		User user = createUser(2L, toBigDecimal(10));
		RequestOrderCriteria criteria = mock(RequestOrderCriteria.class);

		when(orderService.create(any())).thenReturn(order);
		when(criteria.userId()).thenReturn(2L);
		when(criteria.orderItems()).thenReturn(List.of(new Item(1L, 10)));
		when(productService.findById(1L)).thenReturn(createProduct(1L));
		when(userService.get(2L)).thenReturn(user);
		lenient().when(payment.getStatus()).thenReturn(PaymentStatus.COMPLETED);

		// when & then
		CustomException customException = assertThrows(CustomException.class, () -> {
			orderFacade.order(criteria);
		});
		assertEquals(ErrorCode.INSUFFICIENT_POINTS, customException.getErrorCode());
	}

	@Test
	void 결제_실패_시_예외_발생() {
		// given
		RequestOrderCriteria criteria = mock(RequestOrderCriteria.class);
		Payment payment = new Payment(
			null, PaymentMethod.POINTS, LocalDateTime.now(), null, PaymentStatus.FAILED
		);
		User user = createUser(1L, toBigDecimal(10000));
		Order order = createOrder(payment);

		when(criteria.userId()).thenReturn(1L);
		when(criteria.orderItems()).thenReturn(List.of(new Item(1L, 10)));
		when(productService.findById(1L)).thenReturn(createProduct(1L));
		when(userService.get(1L)).thenReturn(user);
		when(orderService.create(any())).thenReturn(order);
		when(paymentService.pay(any(RequestPaymentCommand.class)))
			.thenThrow(new CustomException(PAYMENT_FAIL));

		// when & then
		CustomException customException = assertThrows(CustomException.class, () -> {
			orderFacade.order(criteria);
		});

		assertEquals(PAYMENT_FAIL, customException.getErrorCode());
	}


	@Test
	void 잘못된_수량_요청_시_예외_발생() {
		// given
		RequestOrderCriteria criteria = Mockito.mock(RequestOrderCriteria.class);
		List<Item> invalidOrderItems = List.of(
			new Item(1L, -1) // 잘못된 수량
		);
		Payment payment = mock(Payment.class);
		Order order = createOrder(payment);

		when(criteria.userId()).thenReturn(1L);
		when(userService.get(1L)).thenReturn(createUser(1L, toBigDecimal(10000)));
		lenient().when(productService.findById(1L)).thenReturn(createProduct(1L));
		lenient().when(orderService.create(any())).thenReturn(order);
		when(criteria.orderItems()).thenReturn(invalidOrderItems);

		// when & then
		CustomException customException = assertThrows(CustomException.class, () -> {
			orderFacade.order(criteria);
		});

		assertEquals(ErrorCode.INVALID_QUANTITY, customException.getErrorCode());
	}
}
