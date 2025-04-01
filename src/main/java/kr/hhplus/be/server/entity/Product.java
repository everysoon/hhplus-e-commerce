package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.enums.Category;
import kr.hhplus.be.server.enums.ProductStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    private String productName;
    private Integer stock;
    private Category category;
    private String description;
    private Float price;
    /**
     * stock이 0인 경우 status를 out_of_stock로 자동으로 설정되도록 로직을 추가하거나, 상태를 관리할 때 재고를 기반으로 상태를 관리하는 로직을 추가하자
     * */
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    @CreatedDate
    private LocalDateTime createdAt;
}
