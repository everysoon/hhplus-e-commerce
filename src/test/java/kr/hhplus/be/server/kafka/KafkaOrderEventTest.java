package kr.hhplus.be.server.kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.order.handler.CancelOrderEventHandler;
import kr.hhplus.be.server.application.order.handler.OrderPlacedEventHandler;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.event.CancelOrderEvent;
import kr.hhplus.be.server.domain.order.event.OrderPlacedEvent;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Slf4j
@EmbeddedKafka(partitions = 1, topics = {"order.placed.paid", "order.canceled.paid"})
@SpringBootTest
@EnableKafka
@ActiveProfiles("test")
public class KafkaOrderEventTest {
	@Autowired
	private KafkaTemplate<String, Object> kafkaTemplate;

	@Autowired
	private OrderPlacedEventHandler orderPlacedEventHandler;

	@Autowired
	private CancelOrderEventHandler cancelOrderEventHandler;

	@Autowired
	private EmbeddedKafkaBroker embeddedKafkaBroker;
	private ObjectMapper objectMapper = new ObjectMapper();

	private Consumer<String, String> consumer;

	@BeforeEach
	void setUp() {
		Map<String, Object> consumerProps = KafkaTestUtils.consumerProps("testGroup", "false", embeddedKafkaBroker);
		consumer = new KafkaConsumer<>(consumerProps, new StringDeserializer(), new StringDeserializer());
		consumer.subscribe(List.of("order.placed.paid", "order.canceled.paid"));
	}

	@AfterEach
	void tearDown() {
		consumer.close();
	}

	@Test
	void 주문_생성_이벤트를_발행하면_order_placed_paid_토픽에_메시지가_들어간다() {
		// given
		Order order = mock(Order.class);
		when(order.getTotalPrice()).thenReturn(BigDecimal.valueOf(5000));
		when(order.getId()).thenReturn(1L);
		User user = mock(User.class);
		when(user.getId()).thenReturn(1L);
		OrderPlacedEvent event = OrderPlacedEvent.of(
			user.getId(),
			order,
			PaymentMethod.POINTS,
			UUID.randomUUID().toString()
		);

		// when
		orderPlacedEventHandler.handle(event);

		// then
		ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "order.placed.paid");
		assertNotNull(record);
		log.info("record.value() : {}", record.value());
//		assertTrue(.contains("order123"));
	}

	@Test
	void 주문_취소_이벤트를_발행하면_order_canceled_paid_토픽에_메시지가_들어간다() {
		// given
		Order order = mock(Order.class);
		when(order.getTotalPrice()).thenReturn(BigDecimal.valueOf(5000));
		when(order.getId()).thenReturn(1L);
		User user = mock(User.class);
		when(user.getId()).thenReturn(1L);
		CancelOrderEvent event = CancelOrderEvent.of(
			user.getId(),
			order
		);

		// when
		cancelOrderEventHandler.handle(event);

		// then
		ConsumerRecord<String, String> record = KafkaTestUtils.getSingleRecord(consumer, "order.canceled.paid");
		assertNotNull(record);
		log.info("record.value() : {}", record.value());
//		assertTrue(record.value().toString().contains("order123"));
	}
}
