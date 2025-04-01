package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.dto.order.OrderItemDTO;
import kr.hhplus.be.server.enums.CouponType;
import kr.hhplus.be.server.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Entity
@Table(name = "order_items")
@NoArgsConstructor
@AllArgsConstructor
@Builder
// 주문이 들어간 products
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 가격 리턴할땐 price - couponDiscountAmount;
    private BigDecimal couponDiscountAmount; // 할인 할 가격
    private BigDecimal price; // 기존 가격
    private Integer quantity;



    public OrderItemDTO toDTO(Long orderId) {
        return OrderItemDTO.builder()
                .orderId(orderId)
                .productId(product.getId())
                .productName(product.getProductName())
//                .price(coupon != null && !coupon.isEmpty() ? price.subtract(applyCouponDiscount()) : price)
                .quantity(quantity)
                .couponDiscountAmount(couponDiscountAmount)
                .build();
    }
}
