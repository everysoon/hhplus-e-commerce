package kr.hhplus.be.server.domain.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.infra.user.entity.PointStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointHistory {
	private Long id;
	private final Long userId;
	private final PointStatus status;
	private final BigDecimal price;
	private LocalDateTime createdAt;
	public static PointHistory from(Long userId, BigDecimal price,PointStatus status) {
		return new PointHistory(
			null,
			userId,
			status,
			price,
			LocalDateTime.now()
		);
	}
}
