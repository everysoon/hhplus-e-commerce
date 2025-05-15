package kr.hhplus.be.server.support.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;
import java.util.function.Supplier;

@Component
@Slf4j
public class RetryHandler {

	public <T> void runWithRetry(Supplier<T> action, Consumer<Exception> onFailure, int maxRetry) {
		int retry = 0;
		while (retry < maxRetry) {
			try {
				action.get();
				return;
			} catch (Exception e) {
				retry++;
				log.warn("Retry {}/{} 실패 error={}",
					retry, maxRetry, e.getMessage());
				if (retry >= maxRetry) {
					onFailure.accept(e);
				}
				try { Thread.sleep(200); } catch (InterruptedException ignored) {}
			}
		}
	}
}
