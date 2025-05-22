package kr.hhplus.be.server.domain.point;

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
public class PointHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false)
	private Long id;
	private Long userId;
	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private PointStatus status;

	@Column(nullable = false)
	private BigDecimal amount = BigDecimal.ZERO;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;
	public PointHistory(Long userId, PointStatus status, BigDecimal amount) {
		this.userId = userId;
		this.status = status;
		this.amount = amount;
		this.createdAt = LocalDateTime.now();
	}
}
