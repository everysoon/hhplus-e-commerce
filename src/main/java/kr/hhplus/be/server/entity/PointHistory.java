package kr.hhplus.be.server.entity;

import jakarta.persistence.*;
import kr.hhplus.be.server.dto.user.UserResponseDTO;
import kr.hhplus.be.server.enums.PointStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "point_history")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;
    @Enumerated(EnumType.STRING)
    private PointStatus status;
    private BigDecimal price= BigDecimal.ZERO;

    private LocalDateTime createdAt;


}
