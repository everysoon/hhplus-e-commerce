package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderConcurrencyTest extends BaseIntegrationTest {
	@Autowired
	private OrderFacade orderFacade;

	@Test
	void 동시에_여러명이_주문하면_재고가_정상적으로_차감해야된다(){

	}
}
