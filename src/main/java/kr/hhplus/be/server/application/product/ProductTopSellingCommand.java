package kr.hhplus.be.server.application.product;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public record ProductTopSellingCommand(
	LocalDateTime startDate,
	LocalDateTime endDate,
	Pageable pageable
) {
	public LocalDateTime getStartDateOrDefault() {
		return startDate != null ? startDate : LocalDateTime.now().minusDays(3);
	}

	public LocalDateTime getEndDateOrDefault() {
		return endDate != null ? endDate : LocalDateTime.now();
	}
}
