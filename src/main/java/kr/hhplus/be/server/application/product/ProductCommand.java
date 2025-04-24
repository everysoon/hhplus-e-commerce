package kr.hhplus.be.server.application.product;

import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public class ProductCommand {
	public record FilterSearch (
		String name,
		String category,
		String sortBy, // CATEGORY, PRICE, LATEST(최신순)
		String sorted, // DESC, ASC
		boolean soldOut
	){
		public static FilterSearch of(String name, String category, String sortBy, String sorted, boolean soldOut){
			return new FilterSearch(name,category,sortBy,sorted,soldOut);
		}
	}
	public record TopSelling(
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



}
