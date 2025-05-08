package kr.hhplus.be.server.infra.external;

import kr.hhplus.be.server.application.dataplatform.PaymentClient;
import kr.hhplus.be.server.interfaces.dto.PaymentDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class FakePaymentClient  implements PaymentClient {
	private static final Logger log = LoggerFactory.getLogger(FakePaymentClient.class);

	@Override
	public String send(PaymentDTO.PaymentRequest request) {
		log.info(" ### FakePaymentClient send parameter : {}" ,request.toString());
		return "";
	}

	@Override
	public String getToken(PaymentDTO.TokenRequest request) {
		log.info(" ### FakePaymentClient getToken parameter : {}" ,request.toString());
		return "";
	}

	@Override
	public String cancel(PaymentDTO.PaymentRequest request) {
		log.info(" ### FakePaymentClient cancel parameter : {}" ,request.toString());
		return "";
	}
}
