package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

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

    @NotNull
    @Setter
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @NotNull
    private BigDecimal totalDiscount = BigDecimal.ZERO;

    @CreatedDate
    private LocalDateTime createdAt;

    public void addDiscount(BigDecimal discount) {
        if (this.totalDiscount == null) {
            this.totalDiscount = new BigDecimal(0);
        }
        this.totalDiscount = this.totalDiscount.add(discount);
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice.subtract(this.totalDiscount);
    }
//    private LocalDateTime deliveriedAt;
}
