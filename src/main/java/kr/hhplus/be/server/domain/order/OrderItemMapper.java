package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.repository.ProductRepository;
import kr.hhplus.be.server.infra.order.entity.OrderItemEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderItemMapper {
	private final ProductRepository productRepository;
	public OrderItem toDomain(OrderItemEntity entity){
		Product product = productRepository.findById(entity.getProductId());
		return new OrderItem(
			entity.getId(),
			product,
			entity.getOrderId(),
			entity.getQuantity(),
			entity.getUnitPrice()
		);
	}
	public OrderItemEntity toEntity(OrderItem domain){
		return new OrderItemEntity(
			domain.getId(),
			domain.getProduct().getId(),
			domain.getOrderId(),
			domain.getUnitPrice(),
			domain.getQuantity()
		);
	}
}
