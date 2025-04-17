package kr.hhplus.be.server.domain.point;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import kr.hhplus.be.server.application.point.UpdatePointCommand;
import kr.hhplus.be.server.infra.point.entity.PointStatus;
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

	public static PointHistory from(UpdatePointCommand.Charge command) {
		return new PointHistory(
			null,
			command.getUser().getId(),
			command.getStatus(),
			command.getAmount(),
			LocalDateTime.now()
		);
	}

	public static PointHistory from(UpdatePointCommand.Use command) {
		return new PointHistory(
			null,
			command.getUser().getId(),
			command.getStatus(),
			command.getAmount(),
			LocalDateTime.now()
		);
	}
}
