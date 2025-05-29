package kr.hhplus.be.server.kafka;

import kr.hhplus.be.server.support.aop.event.idempotent.Idempotent;
import kr.hhplus.be.server.support.common.exception.CustomException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class IdempotentAspectTest {

	@Autowired
	SampleService sampleService;

	@Test
	void 중복_요청이면_예외발생() {
		String key = "중복키";
		// 처음 호출 → 정상 처리
		sampleService.sampleMethod(key);
		// 두 번째 호출 → CustomException 터져야 함
		assertThrows(CustomException.class, () -> sampleService.sampleMethod(key));
	}

	@Service
	static class SampleService {
		@Idempotent(key = "#key")
		public String sampleMethod(String key) {
			return "ok";
		}
	}
}
