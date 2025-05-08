//package kr.hhplus.be.server.integration;
//
//import kr.hhplus.be.server.application.coupon.*;
//import kr.hhplus.be.server.domain.coupon.*;
//import kr.hhplus.be.server.domain.user.repository.UserCouponRepository;
//import kr.hhplus.be.server.integration.common.BaseIntegrationTest;
//import kr.hhplus.be.server.integration.common.TestBatchDataFactory;
//import kr.hhplus.be.server.support.common.exception.CustomException;
//import kr.hhplus.be.server.support.config.swagger.ErrorCode;
//import kr.hhplus.be.server.utils.CouponTestFixture;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.List;
//import java.util.UUID;
//
//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
//import static org.junit.jupiter.api.Assertions.assertAll;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//
//public class CouponServiceTest extends BaseIntegrationTest {
//
//	private TestBatchDataFactory dataFactory;
//
//	@Autowired
//	private JdbcTemplate jdbcTemplate;
//
//	@Autowired
//	private CouponRepository couponRepository;
//
//	@Autowired
//	private UserCouponRepository userCouponRepository;
//
//	@Autowired
//	private CouponValidator couponValidator;
//
//	private CouponService couponService;
//	private Coupon coupon;
//	private String couponId;
//	@BeforeAll
//	public void setup(){
//		couponId = UUID.randomUUID().toString();
//		coupon = CouponTestFixture.create(couponId);
//		couponRepository.issue(coupon);
//		couponService = new CouponService(couponRepository, userCouponRepository, couponValidator);
//	}
//
//	@Test
//	void bulkInitTest() {
//		List<Coupon> coupons = couponRepository.findAll();
//		assertThat(coupons.size()).isEqualTo(1000);
//	}
//
//	@Test
//	void 쿠폰_ID로_쿠폰을_조회했을때_해당_쿠폰을_리턴한다() {
//		Coupon coupon = couponService.findCouponById(couponId);
//		assertThat(coupon).isNotNull();
//		assertAll("쿠폰 정보 확인",
//			() -> assertThat(coupon.getType()).isEqualTo(CouponType.FIXED),
//			() -> assertThat(coupon.getDescription()).isEqualTo("TEST Coupon DESC"),
//			() -> assertThat(coupon.getDiscountAmount()).isEqualTo(1000.00),
//			() -> assertThat(coupon.getInitialQuantity()).isEqualTo(100),
//			() -> assertThat(coupon.getRemainingQuantity()).isEqualTo(100),
//			() -> assertThat(coupon.getExpiredAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).isEqualTo(
//				LocalDateTime.now().plusDays(7).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))),
//			() -> assertThat(coupon.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))).isEqualTo(
//				LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")))
//		);
//	}
//
//	@Test
//	void 쿠폰ID로_쿠폰을_조회했을때_존재하지_않을경우_Throws_NOT_EXIST_COUPON() {
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			couponService.findCouponById(UUID.randomUUID().toString());
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.NOT_EXIST_COUPON);
//	}
//
//	@Test
//	void 쿠폰ID로_쿠폰을_조회했을때_쿠폰ID가_UUID형식이_아니라면_Throws_INVALID_COUPON_ID() {
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			couponService.findCouponById("test-coupon-id");
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INVALID_COUPON_ID);
//	}
//
//	@Test
//	void 유저_쿠폰_정상_발급() {
//		// Given
//		Long userId = 1L;
//		IssueCouponCommand command = IssueCouponCommand.of(userId, couponId);
//		// When
//		UserCoupon issuedCoupon = couponService.issueByUser(command);
//
//		// Then
//		assertThat(issuedCoupon).isNotNull();
//		assertAll("유저 쿠폰 정보 확인",
//			() -> assertThat(issuedCoupon.getCoupon().getId()).isEqualTo(couponId),
//			() -> assertThat(issuedCoupon.getUserId()).isEqualTo(userId),
//			() -> assertThat(issuedCoupon.getStatus()).isEqualTo(CouponStatus.ISSUED)
//		);
//	}
//
//	@Test
//	void 유저_쿠폰_발급시_중복_발급이면_Throws_DUPLICATE_COUPON_CLAIM() {
//		// Given
//		Long userId = 1L;
//		IssueCouponCommand command = IssueCouponCommand.of(userId, couponId);
//		// When & Then
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			couponService.issueByUser(command);
//			couponService.issueByUser(command);
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.DUPLICATE_COUPON_CLAIM);
//	}
//
//	@Test
//	void 정상_유저_쿠폰_사용() {
//		// given
//
//		UseCouponCommand command = UseCouponCommand.of(1L,
//			List.of(couponId));
//		// when
//		UseCouponInfo couponInfo = couponService.use(command);
//		// then
//		assertAll("유저 쿠폰 정보 확인",
//			() -> assertThat(couponInfo.userId()).isEqualTo(1L),
//			() -> assertThat(couponInfo.coupons().size()).isEqualTo(1)
//		);
//	}
//
//	@Test
//	@DisplayName("x")
//	void 유저_쿠폰_사용시_유저_소유가_아닌_쿠폰_사용시_Throws_INVALID_USER_COUPON() {
//		// given
//
//		UseCouponCommand command = UseCouponCommand.of(1L,
//			List.of(couponId));
//
//		// when & then
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			couponService.use(command);
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.INVALID_USER_COUPON);
//	}
//
//	@Test
//	@DisplayName("x")
//	void 유저_쿠폰_사용시_사용된_쿠폰의_ID면_Throws_USED_COUPON() {
//		// given
//		UseCouponCommand command = UseCouponCommand.of(1L,
//			List.of("001327f5-7777-4050-b658-fea24ed4e5bd"));
//
//		// when & then
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			couponService.use(command);
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.USED_COUPON);
//	}
//
//	@Test
//	@DisplayName("x")
//	public void 유저_쿠폰_사용시_관리자가_취소하거나_사용할_수_없는_상태의_쿠폰이면_Throws_REVOKED_COUPON() {
//		//given
//		UseCouponCommand command = UseCouponCommand.of(1L,
//			List.of("0012a2e8-90b5-49c3-b3c8-f11c5dc22149"));
//
//		// when & then
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			couponService.use(command);
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.USED_COUPON);
//	}
//
//	@Test
//	@DisplayName("x")
//	public void 유저_쿠폰_사용시_만료된_쿠폰_ID면_Throws_EXPIRED_COUPON() {
//		//given
//		UseCouponCommand command = UseCouponCommand.of(1L,
//			List.of("00180674-2371-4031-9946-7c3e218f516c"));
//
//		// when & then
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			couponService.use(command);
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.USED_COUPON);
//	}
//
//	@Test
//	@DisplayName("x")
//	void 주문취소로_인한_쿠폰_정상_복원() {
//		//given
//		String usedCouponId = "001327f5-7777-4050-b658-fea24ed4e5bd";
//		Long userId = 1L;
//		RestoreCouponCommand command = RestoreCouponCommand.of(userId,
//			usedCouponId);
//		Integer beforeRemainingQuantity = couponService.findCouponById(usedCouponId).getRemainingQuantity();
//		//when
//		couponService.restore(command);
//		Integer afterRemainingQuantity = couponService.findCouponById(usedCouponId).getRemainingQuantity();
//		//then
//		List<String> userCouponIds = couponService.findUserCouponByUserId(userId).stream()
//			.filter(uc -> uc.getStatus() == CouponStatus.ISSUED)
//			.map(UserCoupon::getCoupon)
//			.map(Coupon::getId)
//			.peek(System.out::println)
//			.toList();
//		assertAll("쿠폰 복원 상태 체크",
//			() -> assertThat(userCouponIds.size()).isGreaterThan(0),
//			() -> assertThat(afterRemainingQuantity).isEqualTo(beforeRemainingQuantity+1),
//			() -> assertThat(userCouponIds.contains(usedCouponId)).isTrue()
//		);
//	}
//	@Test
//	@DisplayName("x")
//	void 주문취소로_인한_쿠폰복원시_쿠폰_상태가_관리자에_의해_삭제된_쿠폰이면_Throws_REVOKED_COUPON() {
//		//given
//		UseCouponCommand command = UseCouponCommand.of(1L,
//			List.of("0012a2e8-90b5-49c3-b3c8-f11c5dc22149"));
//
//		// when & then
//		CustomException customException = assertThrows(CustomException.class, () -> {
//			couponService.restore(command);
//		});
//		assertThat(customException.getErrorCode()).isEqualTo(ErrorCode.REVOKED_COUPON);
//	}
//	@Test
//	@DisplayName("x")
//	void 유저_쿠폰_조회_정상_리턴() {
//		//given
//		Long userId = 1L;
//
//		// when
//		List<UserCouponDetailResult> userCoupons = couponService.getUserCoupons(userId);
//
//		// then
//		assertThat(userCoupons.size()).isEqualTo(4);
//	}
//}
