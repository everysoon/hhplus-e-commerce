package kr.hhplus.be.server.infra.order.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.infra.product.entity.ProductEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "order_items")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// 주문이 들어간 products
public class OrderItemEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private ProductEntity productEntity;
    @ManyToOne
    @JoinColumn(name = "order_id") // FK 컬럼
    private OrderEntity orderEntity;
    @Column(nullable = false)
    private Integer quantity;


}
