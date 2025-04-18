package kr.hhplus.be.server.application.payment;

import jakarta.transaction.Transactional;
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

@Service
@RequiredArgsConstructor
public class PaymentService {
	private final Logger logger = LoggerFactory.getLogger(PaymentService.class);
	private final PaymentClient paymentClient;
	private final PaymentRepository paymentRepository;
	private final PaymentHistoryRepository paymentHistoryRepository;

	@Transactional
	public Payment pay(RequestPaymentCommand command) {
//		try {
			// client 통신
			String token = paymentClient.getToken(PaymentDTO.TokenRequest.from(command));
			String transactionId = paymentClient.send(PaymentDTO.PaymentRequest.from(command, token));
			// create payment + payment history
			Payment payment = Payment.of(command,transactionId);
			CreatePaymentHistoryCommand payloadCommand = command.toCreatePaymentCommand(payment);
			// save
			saveWithHistory(payment,PaymentHistory.of(payloadCommand));
			return payment;
//		} catch (Exception e) {
//			logger.error("결제 처리 중 예상치 못한 오류", e);
//			throw new CustomException(ErrorCode.PAYMENT_FAIL);
//		}
	}

	@Transactional
	public Payment cancel(Order order) {
		// client 통신
		Payment payment = paymentRepository.findByOrderId(order.getId());
		RequestPaymentCommand command = RequestPaymentCommand.of(order,payment.getPaymentMethod());
		String token = paymentClient.getToken(PaymentDTO.TokenRequest.from(command));
		String transactionId = paymentClient.cancel(PaymentDTO.PaymentRequest.from(command, token));
		payment.cancel(order.getTotalPrice(),transactionId);
		CreatePaymentHistoryCommand payloadCommand = command.toCreatePaymentCommand(payment);

		saveWithHistory(payment,PaymentHistory.of(payloadCommand));
		return payment;
	}
	public void saveWithHistory(Payment payment,PaymentHistory paymentHistory) {
		paymentRepository.save(payment);
//		System.out.println("saveWithHistory payment : "+ save.getId());
		paymentHistoryRepository.save(paymentHistory);
	}
}
