package kr.hhplus.be.server.unit.domain;

import static kr.hhplus.be.server.utils.TestFixture.toBigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import kr.hhplus.be.server.utils.TestFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User Domain Test")
public class UserTest {
	@Test
	public void 포인트_충전_정상(){
		// given
		User user = TestFixture.createUser(1L, BigDecimal.ZERO);
		// when
		user.charge(toBigDecimal(1000));
		// then
		assertEquals(toBigDecimal(1000),user.getPoint());
	}
	@Test
	public void 포인트_사용_정상(){
		// given
		User user = TestFixture.createUser(1L, toBigDecimal(1000));
		// when
		user.use(toBigDecimal(900));
		// then
		assertEquals(toBigDecimal(100), user.getPoint());
	}
	@Test
	public void 포인트_사용_사용량이_더_많을_때_오류_발생(){
		// given
		User user = TestFixture.createUser(1L, toBigDecimal(1000));
		// when
		CustomException customException = assertThrows(CustomException.class, () -> {
			user.use(toBigDecimal(1000));
		});
		// then
		assertEquals(ErrorCode.INSUFFICIENT_POINTS, customException.getErrorCode());
	}
}
