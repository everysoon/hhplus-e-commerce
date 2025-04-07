package kr.hhplus.be.server.domain.user;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.infra.user.entity.PointStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PointHistory {
	private final Long id;
	private final Long userId;
	private final PointStatus status;
	private final BigDecimal price;
	private LocalDateTime createdAt;
}
