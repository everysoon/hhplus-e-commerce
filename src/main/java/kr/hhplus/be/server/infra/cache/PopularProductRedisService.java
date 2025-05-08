package kr.hhplus.be.server.infra.cache;

import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PopularProductRedisService {
	private final PopularProductRepository popularProductRepository;
	// 인기 상품 점수 증가
	public void increaseScore(Long productId, double score) {
		popularProductRepository.increaseScore(productId, score);
	}

	// 인기 상품 점수 감소
	public void decreaseScore(Long productId, double score) {
		popularProductRepository.decreaseScore(productId, score);
	}

	// 인기 상품 목록 조회
	public List<Long> getTopPopularProductIds(LocalDate startDate, LocalDate endDate, int topN) {
		return popularProductRepository.getTopPopularProductIds(startDate, endDate, topN);
	}

	// 특정 날짜의 인기 상품 캐시 삭제
	public void evictProductCache(LocalDate searchDate) {
		popularProductRepository.evictProductCache(searchDate);
	}
}
