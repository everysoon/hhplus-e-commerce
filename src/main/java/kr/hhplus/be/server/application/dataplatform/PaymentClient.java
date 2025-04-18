package kr.hhplus.be.server.application.dataplatform;

import kr.hhplus.be.server.interfaces.dto.PaymentDTO;

public interface PaymentClient {
	String send(PaymentDTO.PaymentRequest request);
	String getToken(PaymentDTO.TokenRequest request);
	String cancel(PaymentDTO.PaymentRequest request);
}
