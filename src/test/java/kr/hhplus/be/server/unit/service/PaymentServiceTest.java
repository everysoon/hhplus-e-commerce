package kr.hhplus.be.server.unit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import kr.hhplus.be.server.application.dataplatform.PaymentClient;
import kr.hhplus.be.server.application.payment.PaymentService;
import kr.hhplus.be.server.application.payment.RequestPaymentCommand;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentHistory;
import kr.hhplus.be.server.domain.payment.repository.PaymentHistoryRepository;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.interfaces.dto.PaymentDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

	@Mock
	private PaymentClient paymentClient;
	@Mock private PaymentRepository paymentRepository;
	@Mock private PaymentHistoryRepository paymentHistoryRepository;

	@InjectMocks
	private PaymentService paymentService;

	@Test
	void 결제_정상작동() {
		// given
		RequestPaymentCommand command = Mockito.mock(RequestPaymentCommand.class);
		PaymentDTO.TokenRequest tokenRequest = Mockito.mock(PaymentDTO.TokenRequest.class);
		PaymentDTO.PaymentRequest paymentRequest = Mockito.mock(PaymentDTO.PaymentRequest.class);

		String fakeToken = "fake_token";
		String transactionId = "txn_12345";

		Payment payment = Mockito.mock(Payment.class);
		PaymentHistory history = Mockito.mock(PaymentHistory.class);

		// stub: PaymentDTO 관련 static 팩토리 메서드 mocking
		mockStatic(PaymentDTO.TokenRequest.class)
			.when(() -> PaymentDTO.TokenRequest.from(command))
			.thenReturn(tokenRequest);

		mockStatic(PaymentDTO.PaymentRequest.class)
			.when(() -> PaymentDTO.PaymentRequest.from(command, fakeToken))
			.thenReturn(paymentRequest);

		mockStatic(Payment.class)
			.when(() -> Payment.of(command.order(), command.paymentMethod()))
			.thenReturn(payment);

		mockStatic(PaymentHistory.class)
			.when(() -> PaymentHistory.of(payment, transactionId))
			.thenReturn(history);

		when(paymentClient.getToken(tokenRequest)).thenReturn(fakeToken);
		when(paymentClient.send(paymentRequest)).thenReturn(transactionId);

		// when
		Payment result = paymentService.pay(command);

		// then
		assertNotNull(result);
		assertEquals(payment, result);

		verify(paymentClient).getToken(tokenRequest);
		verify(paymentClient).send(paymentRequest);
		verify(paymentHistoryRepository).save(history);
		verify(paymentRepository).save(payment);
	}
}

