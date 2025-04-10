package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.dataplatform.PaymentClient;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentHistory;
import kr.hhplus.be.server.domain.payment.repository.PaymentHistoryRepository;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.interfaces.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentService {
	private final PaymentClient paymentClient;
	private final PaymentRepository paymentRepository;
	private final PaymentHistoryRepository paymentHistoryRepository;

	public Payment pay(RequestPaymentCommand command) {
		String token = paymentClient.getToken(PaymentDTO.TokenRequest.from(command));
		String transactionId =  paymentClient.send(PaymentDTO.PaymentRequest.from(command, token));
		Payment payment =  Payment.of(command.order(),command.paymentMethod());
		PaymentHistory history = PaymentHistory.of(payment,transactionId);
		paymentHistoryRepository.save(history);
		paymentRepository.save(payment);

		return payment;
	}

	public Payment cancel() {
		return null;
	}
}
