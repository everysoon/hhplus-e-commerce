package kr.hhplus.be.server.integration.common;

import static java.util.stream.Collectors.toList;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.product.ProductStatus;
import kr.hhplus.be.server.infra.product.entity.Category;
import net.datafaker.Faker;
import org.springframework.jdbc.core.JdbcTemplate;

public class TestBatchDataFactory {

	private static JdbcTemplate jdbcTemplate;
	private static Faker faker;
	private static final int BATCH_SIZE = 1000;

	public TestBatchDataFactory(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
		this.faker = new Faker();
	}

	public void insertBulkCoupons(int count) {
		// 데이터를 생성하고 배치로 나누어 처리
		Instant start = Instant.now();
		for (int i = 0; i < count; i += BATCH_SIZE) {
			List<Object[]> batchArgs = generateBatchCoupons(Math.min(BATCH_SIZE, count - i));

			System.out.println("### temp coupon row: " + Arrays.toString(batchArgs.get(2)));

			jdbcTemplate.batchUpdate(
				"INSERT INTO coupons (id,type, description, discount_amount, initial_quantity, remaining_quantity, expired_at, issued_at) "
					+
					"VALUES (?,?, ?, ?, ?, ?, ?, ?)",
				batchArgs
			);
		}
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);

		System.out.println("### insertBulkCoupons " + count + "개 :" + duration.toMillis() + "ms");
	}

	public void insertBulkProducts(int count) {
		// 데이터를 생성하고 배치로 나누어 처리
		Instant start = Instant.now();
		for (int i = 0; i < count; i += BATCH_SIZE) {
			List<Object[]> batchArgs = generateBatchProducts(Math.min(BATCH_SIZE, count - i));
			System.out.println("### temp product row: " + Arrays.toString(batchArgs.get(2)));
			jdbcTemplate.batchUpdate(
				"INSERT INTO products (product_name, stock, category, description, price, status, created_at) "
					+
					"VALUES (?, ?, ?, ?, ?, ?, ?)",
				batchArgs
			);
		}
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);

		System.out.println("### insertBulkProducts " + count + "개 :" + duration.toMillis() + "ms");
	}

	public void insertBulkUsers(int count) {
		Instant start = Instant.now();
		for (int i = 0; i < count; i += BATCH_SIZE) {
			List<Object[]> batchArgs = generateBulkUsers(Math.min(BATCH_SIZE, count - i));
			System.out.println("### temp user row: " + Arrays.toString(batchArgs.get(2)));
			jdbcTemplate.batchUpdate(
				"INSERT INTO users (name, email, address, created_at)"
					+ " VALUES (?, ?, ?, ?) ",
				batchArgs
			);
		}
		Instant end = Instant.now();
		Duration duration = Duration.between(start, end);

		System.out.println("### insertBulkUsers " + count + "개 :" + duration.toMillis() + "ms");
	}

	private List<Object[]> generateBatchProducts(int batchSize) {
		return IntStream.range(0, batchSize)
			.mapToObj(i -> new Object[]{
				faker.commerce().productName(),               // productName
				faker.number().numberBetween(1, 1000),        // stock
				faker.options().option(Category.values()).name(), // category (enum)
				faker.lorem().sentence(),                     // description
				BigDecimal.valueOf(faker.number().numberBetween(100, 10000)), // price
				faker.options().option(ProductStatus.values()).name(), // status (enum)
				Timestamp.valueOf(LocalDateTime.now())        // createdAt
			})
			.peek(o->System.out.println(Arrays.toString(o)))
			.collect(toList());
	}

	private List<Object[]> generateBulkUsers(int batchSize) {
		return IntStream.range(0, batchSize)
			.mapToObj(i -> new Object[]{
				faker.name().fullName(),               // 이름
				faker.internet().emailAddress(),       // 이메일
				faker.address().fullAddress(),         // 주소
				Timestamp.valueOf(LocalDateTime.now()) // createdAt (자동으로 현재 시간)
			})
			.peek(o->System.out.println(Arrays.toString(o)))
			.collect(Collectors.toList());
	}

	private List<Object[]> generateBatchCoupons(int batchSize) {
		System.out.println("UUID ?" + UUID.randomUUID());
		System.out.println("UUID ?" + UUID.randomUUID().toString().length());
		return IntStream.range(0, batchSize)
			.mapToObj(i -> new Object[]{
				UUID.randomUUID().toString(),
				faker.options().option(CouponType.values()).name(),  // CouponType
				faker.lorem().sentence(),  // description
				BigDecimal.valueOf(faker.number().numberBetween(1000, 10000)),  // discountAmount
				faker.number().numberBetween(1, 1000),  // initialQuantity
				faker.number().numberBetween(1, 1000),  // remainingQuantity
				LocalDateTime.now().plusDays(7),  // expiredAt
				LocalDateTime.now()  // issuedAt
			})
			.peek(o->System.out.println(Arrays.toString(o)))
			.collect(Collectors.toList());
	}
}
