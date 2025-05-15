package kr.hhplus.be.server.infra.cache;

import kr.hhplus.be.server.support.utils.CacheKeys;
import lombok.RequiredArgsConstructor;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RedissonClient;
import org.redisson.client.RedisException;
import org.redisson.client.codec.LongCodec;
import org.redisson.client.protocol.ScoredEntry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PopularProductRepository {
	private final RedissonClient redissonClient;
	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final long TTL_DAY = 1;
	// 인기 상품 점수 증가
	public void increaseScore(Long productId, double score) {
		String todayKey = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), LocalDate.now());
		RScoredSortedSet<Long> zset = redissonClient.getScoredSortedSet(todayKey, LongCodec.INSTANCE);
		zset.addScore(productId, score);

		// Zset없이 구현할 때의 판매 score 기록
		RMap<Long, Integer> map = redissonClient.getMap(todayKey, LongCodec.INSTANCE);
		map.addAndGet(productId, 1); // 없는 경우 0부터 시작
	}

	// 인기 상품 점수 감소
	public void decreaseScore(Long productId, double score) {
		String todayKey = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), LocalDate.now());
		RScoredSortedSet<Long> zset = redissonClient.getScoredSortedSet(todayKey, LongCodec.INSTANCE);
		zset.addScore(productId, -score);
	}

	// 인기 상품 목록 조회
	public List<Long> getTopPopularProductIds(LocalDate startDate, LocalDate endDate, int topN) {
		Object[] args = new Object[]{startDate, endDate};
		String unionKey = CacheKeys.PRODUCT_UNION.getKey(args);
		RScoredSortedSet<Long> unionZSet = redissonClient.getScoredSortedSet(unionKey, LongCodec.INSTANCE);

		try {
			// 캐시 히트: 이미 집계된 데이터가 있다면 바로 반환
			if (!unionZSet.isEmpty()) {
				return unionZSet.entryRangeReversed(0, topN - 1)
					.stream()
					.map(ScoredEntry::getValue)
					.toList();
			}

			// 캐시 미스: 날짜별 키 집계
			List<String> dateKeys = startDate.datesUntil(endDate.plusDays(1))
				.map(date -> String.format(CacheKeys.POPULAR_PRODUCT.getKey(), date))
				.toList();

			if (dateKeys.isEmpty()) {
				return List.of();
			}

			// union 연산 수행 및 TTL 설정
			redissonClient.getKeys().delete(unionKey); // 기존 키 삭제 (보호적)
			unionZSet.union(dateKeys.toArray(new String[0]));
			unionZSet.expire(Duration.ofDays(TTL_DAY)); // 캐시 TTL 설정

			// Top N 반환
			return unionZSet.entryRangeReversed(0, topN - 1)
				.stream()
				.map(ScoredEntry::getValue)
				.limit(topN)
				.toList();
		} catch (RedisException e) {
			// 예외 발생 시 fallback 처리
			logger.error("Redis 연산 중 오류 발생: {}", e.getMessage(), e);
			throw new RuntimeException("Redis 작업 중 오류가 발생했습니다.", e);
		} catch (Exception e) {
			// 일반 예외 처리
			logger.error("예상치 못한 오류 발생: {}", e.getMessage(), e);
			throw new RuntimeException("예상치 못한 오류가 발생했습니다.", e);
		}
	}

	public List<Long> getTopNPopularProductIdsWithoutZSet(LocalDate startDate, LocalDate endDate, int topN) {
		Object[] args = new Object[]{startDate, endDate};
		String unionKey = CacheKeys.PRODUCT_UNION.getKey(args);
		RMap<Long, Integer> unionMap = redissonClient.getMap(unionKey, LongCodec.INSTANCE);
		if (!unionMap.isEmpty()) {
			return getTopNFromMap(unionMap, topN);
		}
		Map<Long, Integer> aggregateMap = new HashMap<>();

		// 날짜별 key
		List<String> keys = startDate.datesUntil(endDate.plusDays(1))
			.map(date -> String.format("cache:popular:products:hash:%s", date))
			.toList();

		for (String key : keys) {
			RMap<Long, Integer> dailyMap = redissonClient.getMap(key, LongCodec.INSTANCE);

			dailyMap.readAllMap().forEach((productId, count) ->
				aggregateMap.merge(productId, count, Integer::sum)
			);
		}
		if (aggregateMap.isEmpty()) {
			return List.of();
		}
		unionMap.expire(Duration.ofDays(TTL_DAY));
		// Top-N 추출
		return getTopNFromMap(unionMap, topN);
	}

	private List<Long> getTopNFromMap(RMap<Long, Integer> map, int topN) {
		return map.entrySet().stream()
			.sorted(Map.Entry.<Long, Integer>comparingByValue().reversed())
			.limit(topN)
			.map(Map.Entry::getKey)
			.toList();
	}

	// 특정 날짜의 인기 상품 캐시 삭제
	public void evictProductCache(LocalDate searchDate) {
		String todayKey = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), searchDate);
		RScoredSortedSet<Long> zset = redissonClient.getScoredSortedSet(todayKey, LongCodec.INSTANCE);
		zset.delete();
	}


}
