package kr.hhplus.be.server.integration;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;
import kr.hhplus.be.server.application.coupon.CouponService;
import kr.hhplus.be.server.application.coupon.IssueCouponCommand;
import kr.hhplus.be.server.application.coupon.UseCouponCommand;
import kr.hhplus.be.server.application.coupon.UseCouponInfo;
import kr.hhplus.be.server.application.coupon.UserCouponDetailResult;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.coupon.CouponRepository;
import kr.hhplus.be.server.domain.coupon.CouponStatus;
import kr.hhplus.be.server.domain.coupon.CouponType;
import kr.hhplus.be.server.domain.coupon.CouponValidator;
import kr.hhplus.be.server.domain.coupon.UserCoupon;
import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
import kr.hhplus.be.server.integration.common.TestBatchDataFactory;
import kr.hhplus.be.server.support.common.exception.CustomException;
import kr.hhplus.be.server.support.config.swagger.ErrorCode;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;

public class CouponServiceTest extends BaseIntegrationTest {

	private TestBatchDataFactory dataFactory;

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private CouponRepository couponRepository;

	@Autowired
	private UserCouponRepository userCouponRepository;

	@Autowired
	private CouponValidator couponValidator;

	private CouponService couponService;

	@BeforeAll
	public void setup() throws SQLException {
		Connection connection = DriverManager.getConnection("jdbc:tc:mysql:8.0.36:///hhplus");
		System.out.println(">>> Connected to: " + connection.getMetaData().getURL());
		couponService = new CouponService(couponRepository, userCouponRepository, couponValidator);
//
//		dataFactory = new TestBatchDataFactory(jdbcTemplate);
//		dataFactory.insertBulkCoupons(1000);
//		dataFactory.insertBulkUsers(100);
	}

	@Test
	void bulkInitTest() {
		List<Coupon> coupons = couponRepository.findAll();
		assertThat(coupons.size()).isEqualTo(1000);
	}

	@Test
	void 쿠폰_ID로_쿠폰을_조회했을때_해당_쿠폰을_리턴한다() {
		/**
		 * 776ed991-e77b-4e8c-971c-0f3e70ff691e,
		 * FIXED,
		 * Architecto voluptas tempore.,
		 * 4810,
		 * 7,
		 * 547,
		 * 2025-04-25T07:26:16.065107,
		 * 2025-04-18T07:26:16.065110
		 */
		Coupon coupon = couponService.findCouponById("776ed991-e77b-4e8c-971c-0f3e70ff691e");
		assertThat(coupon).isNotNull();
		assertAll("쿠폰 정보 확인",
			() -> assertThat(coupon.getType()).isEqualTo(CouponType.FIXED),
			() -> assertThat(coupon.getDescription()).isEqualTo("Architecto voluptas tempore."),
			() -> assertThat(coupon.getInitialQuantity()).isEqualTo(7),
			() -> assertThat(coupon.getRemainingQuantity()).isEqualTo(547),
			() -> assertThat(coupon.getExpiredAt().toString()).isEqualTo(
				"2025-04-25T07:26:16.065107"),
			() -> assertThat(coupon.getCreatedAt().toString()).isEqualTo(
				"2025-04-18T07:26:16.065110")
		);
	}

	@Test
	void 쿠폰ID로_쿠폰을_조회했을때_존재하지_않을경우_Throws_NOT_EXIST_COUPON() {
		CustomException customException = assertThrows(CustomException.class, () -> {
			couponService.findCouponById(UUID.randomUUID().toString());
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.NOT_EXIST_COUPON);
	}

	@Test
	void 쿠폰ID로_쿠폰을_조회했을때_쿠폰ID가_UUID형식이_아니라면_Throws_INVALID_COUPON_ID() {
		CustomException customException = assertThrows(CustomException.class, () -> {
			couponService.findCouponById("test-coupon-id");
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INVALID_COUPON_ID);
	}

	@Test
	void 유저_쿠폰_정상_발급() {
		// Given
		Long userId = 1L;
		String couponId = "00180674-2371-4031-9946-7c3e218f516c";
		IssueCouponCommand command = IssueCouponCommand.of(userId, couponId);
		// When
		UserCoupon issuedCoupon = couponService.issueByUser(command);

		// Then
		assertThat(issuedCoupon).isNotNull();
		assertAll("유저 쿠폰 정보 확인",
			() -> assertThat(issuedCoupon.getCouponId()).isEqualTo(couponId),
			() -> assertThat(issuedCoupon.getUserId()).isEqualTo(userId),
			() -> assertThat(issuedCoupon.getStatus()).isEqualTo(CouponStatus.ISSUED)
		);
	}

	@Test
	void 유저_쿠폰_발급시_중복_발급이면_Throws_DUPLICATE_COUPON_CLAIM() {
		// Given
		Long userId = 1L;
		String couponId = "000479a5-e942-47a0-86f2-3127d6982bf5";
		IssueCouponCommand command = IssueCouponCommand.of(userId, couponId);
		// When & Then
		CustomException customException = assertThrows(CustomException.class, () -> {
			couponService.issueByUser(command);
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_COUPON_CLAIM);
	}

	@Test
	void 정상_유저_쿠폰_사용() {
		// given
		UseCouponCommand command = UseCouponCommand.of(1L,
			List.of("000479a5-e942-47a0-86f2-3127d6982bf5"));
		// when
		UseCouponInfo couponInfo = couponService.use(command);
		// then
		assertAll("유저 쿠폰 정보 확인",
			() -> assertThat(couponInfo.userId()).isEqualTo(1L),
			() -> assertThat(couponInfo.coupons().size()).isEqualTo(1)
		);
	}

	@Test
	void 유저_쿠폰_사용시_유저_소유가_아닌_쿠폰_사용시_Throws_INVALID_USER_COUPON() {
		// given
		UseCouponCommand command = UseCouponCommand.of(1L,
			List.of("000479a5-e942-47a0-86f2-3127d6982bf1"));

		// when & then
		CustomException customException = assertThrows(CustomException.class, () -> {
			couponService.use(command);
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INVALID_USER_COUPON);
	}

	@Test
	void 유저_쿠폰_사용시_사용된_쿠폰의_ID면_Throws_USED_COUPON() {
		// given
		UseCouponCommand command = UseCouponCommand.of(1L,
			List.of("001327f5-7777-4050-b658-fea24ed4e5bd"));

		// when & then
		CustomException customException = assertThrows(CustomException.class, () -> {
			couponService.use(command);
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.USED_COUPON);
	}

	@Test
	public void 유저_쿠폰_사용시_관리자가_취소하거나_사용할_수_없는_상태의_쿠폰이면_Throws_REVOKED_COUPON() {
		//given
		UseCouponCommand command = UseCouponCommand.of(1L,
			List.of("0012a2e8-90b5-49c3-b3c8-f11c5dc22149"));

		// when & then
		CustomException customException = assertThrows(CustomException.class, () -> {
			couponService.use(command);
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.USED_COUPON);
	}

	@Test
	public void 유저_쿠폰_사용시_만료된_쿠폰_ID면_Throws_EXPIRED_COUPON() {
		//given
		UseCouponCommand command = UseCouponCommand.of(1L,
			List.of("00180674-2371-4031-9946-7c3e218f516c"));

		// when & then
		CustomException customException = assertThrows(CustomException.class, () -> {
			couponService.use(command);
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.USED_COUPON);
	}

	@Test
	void 주문취소로_인한_쿠폰_정상_복원() {
		//given
		String usedCouponId = "001327f5-7777-4050-b658-fea24ed4e5bd";
		Long userId = 1L;
		UseCouponCommand command = UseCouponCommand.of(userId,
			List.of(usedCouponId));
		Integer beforeRemainingQuantity = couponService.findCouponById(usedCouponId).getRemainingQuantity();
		//when
		couponService.restore(command);
		Integer afterRemainingQuantity = couponService.findCouponById(usedCouponId).getRemainingQuantity();
		//then
		List<String> userCouponIds = couponService.findUserCouponByUserId(userId).stream()
			.filter(uc -> uc.getStatus() == CouponStatus.ISSUED)
			.map(UserCoupon::getCouponId)
			.peek(System.out::println)
			.toList();
		assertAll("쿠폰 복원 상태 체크",
			() -> assertThat(userCouponIds.size()).isGreaterThan(0),
			() -> assertThat(afterRemainingQuantity).isEqualTo(beforeRemainingQuantity+1),
			() -> assertThat(userCouponIds.contains(usedCouponId)).isTrue()
		);
	}
	@Test
	void 주문취소로_인한_쿠폰복원시_쿠폰_상태가_관리자에_의해_삭제된_쿠폰이면_Throws_REVOKED_COUPON() {
		//given
		UseCouponCommand command = UseCouponCommand.of(1L,
			List.of("0012a2e8-90b5-49c3-b3c8-f11c5dc22149"));

		// when & then
		CustomException customException = assertThrows(CustomException.class, () -> {
			couponService.restore(command);
		});
		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.REVOKED_COUPON);
	}
	@Test
	void 유저_쿠폰_조회_정상_리턴() {
		//given
		Long userId = 1L;

		// when
		List<UserCouponDetailResult> userCoupons = couponService.getUserCoupons(userId);

		// then
		assertThat(userCoupons.size()).isEqualTo(4);
	}
}
