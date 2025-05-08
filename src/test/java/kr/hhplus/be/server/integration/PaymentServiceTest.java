package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.dataplatform.PaymentClient;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.domain.payment.repository.PaymentHistoryRepository;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.springframework.beans.factory.annotation.Autowired;

public class PaymentServiceTest extends BaseIntegrationTest {
	@Autowired
	private PaymentClient paymentClient;
	@Autowired
	private PaymentRepository paymentRepository;
	@Autowired
	private PaymentHistoryRepository paymentHistoryRepository;
	private PaymentService paymentService;

	@BeforeAll
	void setUp(){
		paymentService =  new PaymentService(paymentClient, paymentRepository, paymentHistoryRepository);
	}
//
//	@Test
//	void 주문시_결제가_정상적으로_된다(){
//		Order order = mock(Order.class);
//		when(order.getId()).thenReturn(1L);
//		when(order.getTotalPrice()).thenReturn(BigDecimal.valueOf(10000));
//		PaymentCommand.Request command = PaymentCommand.Request.of(order, PaymentMethod.POINTS);
//		Payment pay = paymentService.pay(command);
//		assertThat(pay.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
//		assertThat(pay.getPaymentMethod()).isEqualTo(PaymentMethod.POINTS);
//		assertThat(pay.getPrice().toString()).isEqualTo("10000.00");
//	}
}
