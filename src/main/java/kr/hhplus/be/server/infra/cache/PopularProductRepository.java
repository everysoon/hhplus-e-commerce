package kr.hhplus.be.server.infra.cache;

import kr.hhplus.be.server.support.utils.CacheKeys;
import lombok.RequiredArgsConstructor;
import org.redisson.api.*;
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

	private static final String BLOCK_FILTER_KEY = "popular:product:block:filter";
	private static final long EXPECTED_INSERTIONS = 10_000L; // 예상 삽입 수
	private static final double FALSE_POSITIVE_PROBABILITY = 0.01; // 허용 오차율 (예시는 0.01 - 1%)

	private static final long TTL_DAY = 1;

	// 인기 상품 점수 증가
	public void increaseScore(Long productId, double score) {
		String todayKey = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), LocalDate.now());
		RScoredSortedSet<Long> zset = redissonClient.getScoredSortedSet(todayKey,
			LongCodec.INSTANCE);
		zset.addScore(productId, score);

		// Zset없이 구현할 때의 판매 score 기록
//		RMap<Long, Integer> map = redissonClient.getMap(todayKey, LongCodec.INSTANCE);
//		map.addAndGet(productId, score);
	}

	// 인기 상품 점수 감소
	public void decreaseScore(Long productId, double score) {
		String todayKey = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), LocalDate.now());
		RScoredSortedSet<Long> zset = redissonClient.getScoredSortedSet(todayKey,
			LongCodec.INSTANCE);
		zset.addScore(productId, -score);
	}

	// 인기 상품 목록 조회
	public List<Long> getTopPopularProductIds(LocalDate startDate, LocalDate endDate, int topN) {
		Object[] args = new Object[]{startDate, endDate};
		String unionKey = CacheKeys.PRODUCT_UNION.getKey(args);
		RScoredSortedSet<Long> unionZSet = redissonClient.getScoredSortedSet(unionKey,
			LongCodec.INSTANCE);

		try {
			// 캐시 히트: 이미 집계된 데이터가 있다면 바로 반환
			if (!unionZSet.isEmpty()) {
				return getTopNFromZSet(unionZSet, topN);
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
			for (String key : dateKeys) {
				RType type = redissonClient.getKeys().getType(key);
				logger.info("### Key: {}, type : {} ", key, (type != null ? type : "NONE"));
			}
			unionZSet.union(dateKeys.toArray(new String[0]));
			unionZSet.expire(Duration.ofDays(TTL_DAY)); // 캐시 TTL 설정

			// Top N 반환
			return getTopNFromZSet(unionZSet, topN);
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

	public List<Long> getTopNPopularProductIdsWithoutZSet(LocalDate startDate, LocalDate endDate,
		int topN) {
		Object[] args = new Object[]{startDate, endDate};
		String unionKey = CacheKeys.PRODUCT_UNION.getKey(args);
		RMap<Long, Long> unionMap = redissonClient.getMap(unionKey, LongCodec.INSTANCE);
		if (!unionMap.isEmpty()) {
			return getTopNFromMap(unionMap, topN);
		}
		Map<Long, Long> aggregateMap = new HashMap<>();

		// 날짜별 key
		List<String> keys = startDate.datesUntil(endDate.plusDays(1))
			.map(date -> String.format("cache:popular:products:hash:%s", date))
			.toList();

		for (String key : keys) {
			RMap<Long, Long> dailyMap = redissonClient.getMap(key, LongCodec.INSTANCE);

			dailyMap.readAllMap().forEach((productId, count) ->
				aggregateMap.merge(productId, count, Long::sum)
			);
		}
		if (aggregateMap.isEmpty()) {
			return List.of();
		}
		unionMap.putAll(aggregateMap);
		unionMap.expire(Duration.ofDays(TTL_DAY));
		// Top-N 추출
		return getTopNFromMap(unionMap, topN);
	}

	private List<Long> getTopNFromZSet(RScoredSortedSet<Long> unionZSet, int topN) {
		return unionZSet.entryRangeReversed(0, topN - 1)
			.stream()
			.map(ScoredEntry::getValue)
			.filter(id -> !getBlockFilter().contains(id)) // 블룸필터 추가
			.limit(topN)
			.toList();
	}

	private List<Long> getTopNFromMap(RMap<Long, Long> map, int topN) {
		return map.entrySet().stream()
			.sorted(Map.Entry.<Long, Long>comparingByValue().reversed())
			.limit(topN)
			.filter(entry -> !getBlockFilter().contains(entry.getKey())) // 블룸필터 추가
			.map(Map.Entry::getKey)
			.toList();
	}

	// 특정 날짜의 인기 상품 캐시 삭제
	public void evictProductCache(LocalDate searchDate) {
		String todayKey = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), searchDate);
		RScoredSortedSet<Long> zset = redissonClient.getScoredSortedSet(todayKey,
			LongCodec.INSTANCE);
		zset.delete();
	}

	private RBloomFilter<Long> getBlockFilter() {
		RBloomFilter<Long> filter = redissonClient.getBloomFilter(BLOCK_FILTER_KEY,
			LongCodec.INSTANCE);
		if (!filter.isExists()) {
			filter.tryInit(EXPECTED_INSERTIONS, FALSE_POSITIVE_PROBABILITY);
		}
		return filter;
	}

	// 따로 블룸필터를 적용해서 product를 제외할때
	public void blockProductFromRanking(Long productId) {
		getBlockFilter().add(productId);
	}
}
