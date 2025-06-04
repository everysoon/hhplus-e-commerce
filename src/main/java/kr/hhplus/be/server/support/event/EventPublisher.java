package kr.hhplus.be.server.support.event;

public interface EventPublisher {
	<T> void publish(String topic, T event);
}
