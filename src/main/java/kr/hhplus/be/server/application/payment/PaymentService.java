package kr.hhplus.be.server.application.payment;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

import kr.hhplus.be.server.application.dataplatform.PaymentClient;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentHistory;
import kr.hhplus.be.server.domain.payment.repository.PaymentHistoryRepository;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.interfaces.dto.PaymentDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PaymentService {
	private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
	private final PaymentClient paymentClient;
	private final PaymentRepository paymentRepository;
	private final PaymentHistoryRepository paymentHistoryRepository;

	@Transactional(propagation = MANDATORY)
	public Payment pay(PaymentCommand.Request command) {
		logger.info("### pay command : {}", command);
		// client 통신
		String token = paymentClient.getToken(PaymentDTO.TokenRequest.from(command));
		String transactionId = paymentClient.send(PaymentDTO.PaymentRequest.from(command, token));
		// create payment + payment history
		Payment payment = Payment.of(command, transactionId);
		PaymentCommand.CreateHistory payloadCommand = command.toCreatePaymentCommand(payment);
		// save
		saveWithHistory(payment, PaymentHistory.of(payloadCommand));
		return payment;
	}

	@Transactional(propagation = MANDATORY)
	public Payment cancel(Order order) {
		// client 통신
		logger.info("### cancel command : {}", order);
		Payment payment = paymentRepository.findByOrderId(order.getId());
		PaymentCommand.Request command = PaymentCommand.Request.of(order, payment.getPaymentMethod());
		String token = paymentClient.getToken(PaymentDTO.TokenRequest.from(command));
		String transactionId = paymentClient.cancel(PaymentDTO.PaymentRequest.from(command, token));
		payment.cancel(order.getTotalPrice(), transactionId);
		PaymentCommand.CreateHistory payloadCommand = command.toCreatePaymentCommand(payment);

		saveWithHistory(payment, PaymentHistory.of(payloadCommand));
		return payment;
	}

	public void saveWithHistory(Payment payment, PaymentHistory paymentHistory) {
		paymentRepository.save(payment);
		paymentHistoryRepository.save(paymentHistory);
	}
}
