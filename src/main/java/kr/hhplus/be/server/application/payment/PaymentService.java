package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.dataplatform.PaymentClient;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.payment.PaymentHistory;
import kr.hhplus.be.server.domain.payment.repository.PaymentHistoryRepository;
import kr.hhplus.be.server.domain.payment.repository.PaymentRepository;
import kr.hhplus.be.server.interfaces.dto.PaymentDTO;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
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

	public Payment pay(RequestPaymentCommand command) {
		try {
			// client 통신
			String token = paymentClient.getToken(PaymentDTO.TokenRequest.from(command));
			String transactionId = paymentClient.send(PaymentDTO.PaymentRequest.from(command, token));
			// create payment + payment history
			Payment payment = Payment.of(command,transactionId);
			CreatePaymentHistoryCommand payloadCommand = command.toCreatePaymentCommand(payment);
			// save
			paymentHistoryRepository.save(PaymentHistory.of(payloadCommand));
			paymentRepository.save(payment);
			return payment;
		} catch (Exception e) {
			logger.error("결제 처리 중 예상치 못한 오류", e);
			throw new CustomException(ErrorCode.PAYMENT_FAIL);
		}
	}

	public Payment cancel() {
		return null;
	}
}
