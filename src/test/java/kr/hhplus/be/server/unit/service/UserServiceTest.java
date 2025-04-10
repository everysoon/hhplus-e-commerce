package kr.hhplus.be.server.unit.service;

import static kr.hhplus.be.server.utils.TestFixture.createUser;
import static kr.hhplus.be.server.utils.TestFixture.toBigDecimal;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.user.PointHistory;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.repository.PointHistoryRepository;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

	@Mock
	private UserRepository userRepository;

	@Mock
	private PointHistoryRepository pointHistoryRepository;

	@InjectMocks
	private UserService userService;

	@Test
	void 유저_포인트_조회_정상작동() {
		// given
		Long userId = 1L;
		User user = createUser(userId,toBigDecimal(1000));
		when(userRepository.findByUserId(userId)).thenReturn(user);

		// when
		User result = userService.getUserPoint(userId);

		// then
		assertNotNull(result);
		assertEquals(user, result);
		assertEquals(user.getPoint(), result.getPoint());
	}
	@Test
	void 포인트_충전시_히스토리_기록되고_유저_저장된다() {
		// given
		Long userId = 1L;
		BigDecimal chargeAmount = toBigDecimal(500);
		User user = spy(createUser(userId,toBigDecimal(1000)));
		when(userRepository.findByUserId(userId)).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);

		// when
		User result = userService.chargePoint(userId, chargeAmount);

		// then
		verify(user).charge(chargeAmount);
		verify(pointHistoryRepository).save(any(PointHistory.class));
		verify(userRepository).save(user);
		assertEquals(user, result);
	}

	@Test
	void 포인트_사용시_히스토리_기록되고_유저_저장된다() {
		// given
		Long userId = 1L;
		BigDecimal useAmount = toBigDecimal(300);
		User user = spy(createUser(userId,toBigDecimal(1000)));
		when(userRepository.findByUserId(userId)).thenReturn(user);
		when(userRepository.save(user)).thenReturn(user);

		// when
		User result = userService.usePoint(userId, useAmount);

		// then
		verify(user).use(useAmount);
		verify(pointHistoryRepository).save(any(PointHistory.class));
		verify(userRepository).save(user);
		assertEquals(user, result);
	}

	@Test
	void 유저_단순조회() {
		// given
		Long userId = 1L;
		User user = createUser(userId,toBigDecimal(1000));
		when(userRepository.findByUserId(userId)).thenReturn(user);

		// when
		User result = userService.get(userId);

		// then
		assertNotNull(result);
		assertEquals(user, result);
	}
}
