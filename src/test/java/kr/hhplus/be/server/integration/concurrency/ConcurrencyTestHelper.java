package kr.hhplus.be.server.integration.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.IntConsumer;

@Component
@Slf4j
public class ConcurrencyTestHelper {
	public List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());

	public List<Exception> getExceptions() {
		return exceptions;
	}

	public void run(int numberOfThreads, IntConsumer taskIndexConsumer) throws InterruptedException {
		ExecutorService executorService = Executors.newFixedThreadPool(Math.min(numberOfThreads, 32));
		CountDownLatch latch = new CountDownLatch(numberOfThreads);

		for (int i = 0; i < numberOfThreads; i++) {
			final int index = i;
			executorService.submit(() -> {
				try {
					taskIndexConsumer.accept(index);
				} catch (Exception e) {
					log.info("### [{}] Concurrency Exception : {}", index, e.getMessage());
					exceptions.add(e);
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();
		executorService.shutdown();
	}
}
