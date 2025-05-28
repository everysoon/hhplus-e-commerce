package kr.hhplus.be.server.integration.concurrency;

import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.application.order.OrderResult;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.payment.PaymentMethod;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.point.PointRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.ProductRepository;
import kr.hhplus.be.server.domain.user.User;
import kr.hhplus.be.server.domain.user.UserRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import kr.hhplus.be.server.utils.ProductTestFixture;
import kr.hhplus.be.server.utils.UserTestFixture;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

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
	@Autowired
	private ConcurrencyTestHelper concurrencyTestHelper;

	int threadCount = 5;
	@Test
	@DisplayName("재고 차감 동시성 이슈")
	void 상품_주문시_상품_재고가_1개일_경우_여러주문이_들어오면_한개만_성공한다() throws InterruptedException {
		User user = UserTestFixture.createUser(1L);
		Product product = ProductTestFixture.create(1L, 1);

		productRepository.save(product);
		userRepository.save(user);
		pointRepository.save(new Point(user.getId(),BigDecimal.valueOf(50000)));

		concurrencyTestHelper.run(threadCount, index -> {
			OrderCriteria.Request request = new OrderCriteria.Request(
				user.getId(), // userId
				List.of(new OrderCriteria.Request.Item(product.getId(), 1)), // 상품 1개 주문
				List.of(), // 쿠폰 없음
				PaymentMethod.POINTS
			);
			OrderResult.Place placeOrderResult = orderFacade.placeOrder(request);
			log.info("### placeOrderResult : {}", placeOrderResult);
		});
		List<Order> allOrders = orderRepository.findAll();
		Product afterProduct = productRepository.findById(product.getId());
		assertThat(afterProduct.getStock()).isEqualTo(0);
		assertThat(allOrders).hasSize(1);
		assertThat(concurrencyTestHelper.getExceptions()).hasSize(4);
	}
}
