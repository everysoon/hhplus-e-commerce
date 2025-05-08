package kr.hhplus.be.server.domain.point;

import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.infra.point.entity.PointStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class PointHistory {

	private Long id;
	private final Long userId;
	private final PointStatus status;
	private final BigDecimal price;
	private LocalDateTime createdAt;

	public static PointHistory from(PointCommand.Charge command) {
		return new PointHistory(
			null,
			command.userId(),
			command.status(),
			command.amount(),
			LocalDateTime.now()
		);
	}

	public static PointHistory from(PointCommand.Use command) {
		return new PointHistory(
			null,
			command.userId(),
			command.status(),
			command.amount(),
			LocalDateTime.now()
		);
	}
}
