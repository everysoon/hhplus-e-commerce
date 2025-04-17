package kr.hhplus.be.server.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import kr.hhplus.be.server.application.dataplatform.PaymentClient;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.application.payment.RequestPaymentCommand;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.payment.repository.PaymentHistoryRepository;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.infra.payment.entity.PaymentStatus;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
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

	@Test
	void 주문시_결제가_정상적으로_된다(){
		Order order = mock(Order.class);
		when(order.getId()).thenReturn(1L);
		when(order.getTotalPrice()).thenReturn(BigDecimal.valueOf(10000));
		RequestPaymentCommand command = RequestPaymentCommand.of(order, PaymentMethod.POINTS);
		Payment pay = paymentService.pay(command);
		assertThat(pay.getStatus()).isEqualTo(PaymentStatus.COMPLETED);
		assertThat(pay.getPaymentMethod()).isEqualTo(PaymentMethod.POINTS);
		assertThat(pay.getPrice().toString()).isEqualTo("10000.00");
	}
}
