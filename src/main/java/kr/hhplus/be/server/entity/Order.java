package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@Table(name = "orders")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL)
    private Payment payment;

//    @OneToMany(mappedBy = "order")
//    private List<Coupon> coupon;
//
//    @OneToMany(mappedBy = "order")
//    private List<OrderItem> orderItem;

    private BigDecimal totalPrice;
    private BigDecimal totalDiscount;

    private LocalDateTime createdAt;
    public void addDiscount(BigDecimal discount) {
        this.totalDiscount = this.totalDiscount.add(discount);
    }
    public void addPrice(BigDecimal price) {
        this.totalPrice = this.totalPrice.add(price);
    }
//    private LocalDateTime deliveriedAt;

//    public BigDecimal applyDiscountAmount(){
//        this.totalPrice = orderItem.stream().map(o->o.getProduct().getPrice())
//            .reduce((x,y)->x.add(y)).get();
//        return coupon.stream()
//            .map(c -> c.calculateDiscount(totalPrice))
//            .reduce((x,y)->x.add(y)).get();
//    }
}
