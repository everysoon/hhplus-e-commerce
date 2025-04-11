package kr.hhplus.be.server.infra.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "point_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PointStatus status;
    @Column(nullable = false)
    private BigDecimal price = BigDecimal.ZERO;
    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;
}
