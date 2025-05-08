package kr.hhplus.be.server.infra.point.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.domain.point.PointHistory;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

@Getter
@Entity
@Table(name = "point_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointHistoryEntity {

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
	public PointHistoryEntity(Long userId,PointStatus status,BigDecimal amount) {
		this.userId = userId;
		this.status = status;
		this.amount = amount;
		this.createdAt = LocalDateTime.now();
	}
	public static PointHistoryEntity from(PointHistory pointHistory) {
		return new PointHistoryEntity(
			pointHistory.getUserId(),
			pointHistory.getStatus(),
			pointHistory.getPrice()
		);
	}
	public PointHistory toDomain() {
		return new PointHistory(
			this.id,
			this.userId,
			this.status,
			this.amount,
			this.createdAt
		);
	}
}
