package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.dataplatform.PaymentClient;
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

import java.math.BigDecimal;

import static org.springframework.transaction.annotation.Propagation.MANDATORY;

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
		Payment payment = Payment.builder()
			.orderId(command.orderId())
			.price(command.price())
			.transactionId(transactionId)
			.paymentMethod(command.paymentMethod())
			.build();
		PaymentCommand.CreateHistory payloadCommand = command.toCreatePaymentCommand(payment);
		// save
		saveWithHistory(payment, new PaymentHistory(payloadCommand));
		return payment;
	}

	@Transactional(propagation = MANDATORY)
	public Payment cancel(Long orderId, BigDecimal totalPrice) {
		// client 통신
		logger.info("### cancel command : orderId = {}, totalPrice = {}", orderId, totalPrice);
		Payment payment = paymentRepository.findByOrderId(orderId);
		PaymentCommand.Request command = PaymentCommand.Request.of(orderId, totalPrice, payment.getPaymentMethod());
		String token = paymentClient.getToken(PaymentDTO.TokenRequest.from(command));
		String transactionId = paymentClient.cancel(PaymentDTO.PaymentRequest.from(command, token));
		payment.cancel(totalPrice, transactionId);
		PaymentCommand.CreateHistory payloadCommand = command.toCreatePaymentCommand(payment);

		saveWithHistory(payment, new PaymentHistory(payloadCommand));
		return payment;
	}

	public Payment findByOrderId(Long orderId) {
		return paymentRepository.findByOrderId(orderId);
	}

	public void saveWithHistory(Payment payment, PaymentHistory paymentHistory) {
		paymentRepository.save(payment);
		paymentHistoryRepository.save(paymentHistory);
	}
}
