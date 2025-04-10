package kr.hhplus.be.server.domain.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kr.hhplus.be.server.domain.coupon.Coupon;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderHistory {
	private final Long id;
	private final Long orderId;
	private final OrderStatus status;
	private final LocalDateTime createdAt;
}
