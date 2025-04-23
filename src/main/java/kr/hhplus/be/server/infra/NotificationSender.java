package kr.hhplus.be.server.infra;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class NotificationSender {
	private final Logger logger = LoggerFactory.getLogger(NotificationSender.class);
	// @Async를 사용하여 비동기 메서드로 설정
	@Async
	public void sendNotification(String message) {
		try {
			// 실제 작업을 2초간 지연시킴 (예: 이메일 전송)
			Thread.sleep(2000);
			logger.info(message);
		} catch (InterruptedException e) {
			logger.error(e.getMessage());
		}
	}
}
