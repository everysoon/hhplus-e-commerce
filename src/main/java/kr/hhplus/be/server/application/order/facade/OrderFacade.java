package kr.hhplus.be.server.application.order.facade;

import kr.hhplus.be.server.application.order.dto.OrderRequestDTO;
import kr.hhplus.be.server.application.order.dto.OrderResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
@RequiredArgsConstructor
public class OrderFacade {

	public OrderResponseDTO order(OrderRequestDTO dto) {
		return null;
	}
}
