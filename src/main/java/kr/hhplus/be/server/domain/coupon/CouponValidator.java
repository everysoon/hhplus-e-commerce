package kr.hhplus.be.server.domain.coupon;

import java.util.UUID;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import org.springframework.stereotype.Component;

@Component
public class CouponValidator {
	public void isCouponIdValidUuid(String id) {
		try {
			UUID.fromString(id);
		} catch (IllegalArgumentException e) {
			throw new CustomException(ErrorCode.INVALID_COUPON_ID);
		}
	}
}
