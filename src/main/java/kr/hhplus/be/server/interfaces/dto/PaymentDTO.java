package kr.hhplus.be.server.interfaces.dto;

import kr.hhplus.be.server.application.payment.RequestPaymentCommand;
import kr.hhplus.be.server.support.utils.CryptoUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class PaymentDTO {
	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class TokenRequest {
		// Hex(sha256(MID + MerchantKey))
		// 결제 정보 암호화
		private String encryptData;

		public static TokenRequest from(RequestPaymentCommand command) {
			return new TokenRequest(CryptoUtil.sha256Hex(command.combineInfo()));
		}
	}

	@Data
	@NoArgsConstructor
	@AllArgsConstructor
	@Builder
	public static class PaymentRequest {
		// Hex(sha256(AuthToken + MID + Amt + MerchantKey))
		// 결제 정보 암호화
		private String encryptData;

		public static PaymentRequest from(RequestPaymentCommand command, String token) {
			return new PaymentRequest(CryptoUtil.sha256Hex(command.combineAll(token)));
		}
	}
}
