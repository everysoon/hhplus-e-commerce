package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.repository.PointRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.repository.UserRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import kr.hhplus.be.server.utils.ProductTestFixture;
import kr.hhplus.be.server.utils.UserTestFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
@Slf4j
public class OrderConcurrencyTest extends BaseIntegrationTest {
	@Autowired
	private OrderFacade orderFacade;
	@Autowired
	private ProductRepository productRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	PointRepository pointRepository;

	Product product;
	int threadCount = 2;
	ExecutorService executor;
	CountDownLatch latch;
	User user;

	@BeforeEach
	void setUp() {
		user = UserTestFixture.createUser(1L);
		product = ProductTestFixture.create(1L,1);

		productRepository.save(product);
		userRepository.save(user);
		pointRepository.save(Point.from(PointCommand.Charge.of(user.getId(),BigDecimal.valueOf(50000))));

		log.info("### temp product : {}", product.getId());
		log.info("### temp user : {}", user.getId());
		executor = Executors.newFixedThreadPool(threadCount);
		latch = new CountDownLatch(threadCount);
	}
	@Test
	void 재고가_부족한상황에서_두개의_주문이_동시에_들어오면_동시성_제어가_없어_둘다_성공한다() throws InterruptedException {
		List<Exception> exceptions = Collections.synchronizedList(new ArrayList<>());
		for (int i = 0; i < threadCount; i++) {
			executor.submit(() -> {
				try {
					OrderCriteria.Request request = new OrderCriteria.Request(
						user.getId(), // userId
						List.of(new OrderCriteria.Request.Item(product.getId(), 1)), // 상품 1개 주문
						List.of(), // 쿠폰 없음
						PaymentMethod.POINTS
					);
					OrderResult.Place placeOrderResult = orderFacade.placeOrder(request);
					log.info("### placeOrderResult : {}", placeOrderResult);
				} catch (Exception e) {
					exceptions.add(e);
					log.info("### exception : {}", e.getMessage());
				} finally {
					latch.countDown();
				}
			});
		}

		latch.await();

		List<Order> allOrders = orderRepository.findAll();
		assertThat(allOrders).hasSize(2); // 하나만 성공해야 함
		assertThat(exceptions).hasSize(0); // 하나는 실패
	}
}
