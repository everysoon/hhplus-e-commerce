package kr.hhplus.be.server.repository;

import kr.hhplus.be.server.entity.Order;
import kr.hhplus.be.server.entity.OrderHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderHistoryRepository extends JpaRepository<OrderHistory, Long> {
}
