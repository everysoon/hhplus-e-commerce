package kr.hhplus.be.server;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kr.hhplus.be.server.infra.cache.PopularProductRedisService;
import kr.hhplus.be.server.integration.common.EmbeddedRedisConfig;
import kr.hhplus.be.server.support.utils.CacheKeys;
import org.junit.jupiter.api.Test;
import org.redisson.api.RMap;
import org.redisson.api.RScoredSortedSet;
import org.redisson.api.RType;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.LongCodec;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(EmbeddedRedisConfig.class)
public class PopularProductRedisServiceTest {

	@Autowired
	private PopularProductRedisService popularProductRedisService;

	@Autowired
	private RedissonClient redissonClient;

//	@BeforeEach
//	void setUp() {
//		// Redis 초기화
//		redissonClient.getKeys().flushall();
//	}

	@Test
	void 인기상품_점수를_증가하고_기간별로_조회할_수_있다() {
		// given
		LocalDate day1 = LocalDate.of(2025, 5, 7);
		LocalDate day2 = LocalDate.of(2025, 5, 8);
		String startDate = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), day1);
		String endDate = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), day2);
		// 직접 날짜별 ZSET에 점수 넣기
		RScoredSortedSet<Long> day1ZSet = redissonClient.getScoredSortedSet(startDate,
			LongCodec.INSTANCE);
		day1ZSet.addScore(101L, 5);
		day1ZSet.addScore(102L, 3);

		RScoredSortedSet<Long> day2ZSet = redissonClient.getScoredSortedSet(endDate,
			LongCodec.INSTANCE);
		day2ZSet.addScore(101L, 2);  // 101의 총합 = 7
		day2ZSet.addScore(103L, 10); // 103의 총합 = 10

		String unionKey = CacheKeys.PRODUCT_UNION.getKey(new Object[]{day1,day2});
		RScoredSortedSet<Long> unionZSet = redissonClient.getScoredSortedSet(unionKey,
			LongCodec.INSTANCE);

		unionZSet.union(startDate,endDate);

		// when
		List<Long> result = popularProductRedisService.getTopPopularProductIds(day1, day2, 2);

		// then
		assertThat(result).containsExactly(103L, 101L); // 점수 높은 순
	}

	@Test
	void 점수를_증가하고_감소할_수_있다() {
		// given
		popularProductRedisService.increaseScore(200L, 5);
		popularProductRedisService.decreaseScore(200L, 2);

		String todayKey = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), LocalDate.now());
		RScoredSortedSet<Long> todayZSet = redissonClient.getScoredSortedSet(todayKey,
			LongCodec.INSTANCE);

		// when
		Double score = todayZSet.getScore(200L);

		// then
		assertThat(score).isEqualTo(3.0);
	}

	@Test
	void 캐시_미스시_정상적으로_union후_데이터조회() {
		LocalDate start = LocalDate.of(2025, 5, 1);
		LocalDate end = LocalDate.of(2025, 5, 3);

		for (LocalDate date : start.datesUntil(end.plusDays(1)).toList()) {
			String key = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), date);
			RScoredSortedSet<Long> zset = redissonClient.getScoredSortedSet(key, LongCodec.INSTANCE);
			zset.add(10.0, 1L);
			zset.add(5.0, 2L);
		}

		// when
		List<Long> topProducts = popularProductRedisService.getTopPopularProductIds(start, end, 2);

		// then
		assertThat(topProducts).containsExactly(1L, 2L);
	}


	@Test
	void 캐시히트_시_unionKey에서_바로조회() {
		// given
		String dateKey = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), LocalDate.now());
		RScoredSortedSet<Long> unionZSet = redissonClient.getScoredSortedSet(
			dateKey, LongCodec.INSTANCE);

		unionZSet.add(20.0, 100L);
		unionZSet.add(15.0, 200L);

		// 캐시가 반영되었는지 확인 (로그를 찍어볼 수도 있음)
		assertThat(unionZSet.size()).isEqualTo(2);

		// when
		List<Long> result = popularProductRedisService.getTopPopularProductIds(LocalDate.now(), LocalDate.now().plusDays(1), 2);

		// then
		assertThat(result).containsExactly(100L, 200L); // 정렬된 결과가 제대로 반환되는지 확인
	}

	@Test
	void 직렬화_실패시_예외처리된다() {
		// given
		String key = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), LocalDate.now());
		RScoredSortedSet<Object> brokenZSet = redissonClient.getScoredSortedSet(key);

		brokenZSet.add(1.0, new Object()); // 직렬화 불가능 객체 삽입

		// when & then
		assertThrows(Exception.class, () -> {
			redissonClient.getScoredSortedSet(key, LongCodec.INSTANCE).entryRangeReversed(0, 1);
		});
	}
	@Test
	void Redis_Hash로_인기상품_조회(){
		// given
		LocalDate day1 = LocalDate.of(2025, 5, 7);
		LocalDate day2 = LocalDate.of(2025, 5, 8);
		String startDateKey = String.format("cache:popular:products:hash:%s", day1);
		String endDateKey = String.format("cache:popular:products:hash:%s", day2);
		List<String> keys = day1.datesUntil(day2.plusDays(1))
			.map(date -> String.format("cache:popular:products:hash:%s", date))
			.toList();
		Map<Long, Long> aggregateMap = new HashMap<>();
		for (String key : keys) {
			RMap<Long, Long> dailyMap = redissonClient.getMap(key, LongCodec.INSTANCE);

			dailyMap.readAllMap().forEach((productId, count) ->
				aggregateMap.merge(productId, count, Long::sum)
			);
		}

		RMap<Long, Long> unionMap = redissonClient.getMap(startDateKey, LongCodec.INSTANCE);
		unionMap.addAndGet(101L, 5);
		unionMap.addAndGet(102L, 3);

		RMap<Long, Long> unionMap2 = redissonClient.getMap(endDateKey,
			LongCodec.INSTANCE);
		unionMap2.addAndGet(101L, 2);  // 101의 총합 = 7
		unionMap2.addAndGet(103L, 10); // 103의 총합 = 10

		// when
		List<Long> result = popularProductRedisService.getTopNPopularProductIdsWithoutZSet(day1, day2, 2);

		// then
		assertThat(result).containsExactly(103L, 101L); // 점수 높은 순
	}
	@Test
	void 블룸필터로_productId1인_상품_제외하기(){
		// given
		popularProductRedisService.blockProductFromRanking(1L);
		LocalDate day1 = LocalDate.of(2025, 5, 17);
		LocalDate day2 = LocalDate.of(2025, 5, 18);
		String startDateKey = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), day1);
		String endDateKey = String.format(CacheKeys.POPULAR_PRODUCT.getKey(), day2);

		redissonClient.getKeys().delete(startDateKey);
		RType type = redissonClient.getKeys().getType(startDateKey);
		String keyType = (type == null) ? "NONE" : type.toString();
		System.out.println("### Key type: " + keyType);

		RScoredSortedSet<Long> day2ZSet = redissonClient.getScoredSortedSet(startDateKey,
			LongCodec.INSTANCE);
		day2ZSet.addScore(1L, 2);
		day2ZSet.addScore(103L, 10);

		String unionKey = CacheKeys.PRODUCT_UNION.getKey(new Object[]{day1,day2});
		RScoredSortedSet<Long> unionZSet = redissonClient.getScoredSortedSet(unionKey,
			LongCodec.INSTANCE);

		unionZSet.union(startDateKey,endDateKey);
		// when
		List<Long> result = popularProductRedisService.getTopPopularProductIds(day1, day2, 2);

		// then
		assertThat(result).containsExactly(103L);
	}
}
