package kr.hhplus.be.server.mock.e2e.user;

import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.response.Response;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.BaseE2ETest;
import kr.hhplus.be.server.dto.product.ProductResponseDTO;
import kr.hhplus.be.server.dto.user.UserCouponResponseDTO;
import kr.hhplus.be.server.enums.Category;
import kr.hhplus.be.server.enums.CouponStatus;
import kr.hhplus.be.server.enums.CouponType;
import kr.hhplus.be.server.enums.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.post;
import static kr.hhplus.be.server.config.swagger.ErrorCode.*;
import static kr.hhplus.be.server.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.*;

public class MockCouponE2ETest extends BaseE2ETest {
	private final String POST_ISSUED_COUPON = "/coupons/{userId}";

	@Test
	@DisplayName("선착순 쿠폰 발급 [200] 쿠폰 정상 발행")
	public void getCoupon() {
		Response response = post(POST_ISSUED_COUPON, userId);
		verifyApiResponseSuccess(response.then());
		UserCouponResponseDTO coupon = parseResponse(response, new TypeReference<ResponseApi<UserCouponResponseDTO>>() {
		});
		// dto값 검증
		assertThat(coupon.getCouponId()).isNotNull();
		assertThat(coupon.getUserId()).isEqualTo(userId);
		assertThat(coupon.getCouponType()).isEqualTo(CouponType.FIXED);
		assertThat(coupon.getDescription()).isEqualTo(defaultCouponDesc);
		assertThat(coupon.getRemainingStock()).isEqualTo(defaultCouponRemainingStock);
		assertThat(coupon.getCouponStatus()).isEqualTo(CouponStatus.ISSUED);
		assertThat(coupon.getIssuedAt()).isEqualTo(now());
	}

	@Test
	@DisplayName("선착순 쿠폰 발급 [400] 유효한 userId가 아닐때")
	public void getCouponErrorWhenInvalidProductId() {
		Response response = post(POST_ISSUED_COUPON, invalidId);
		verifyApiResponseError(response.then(), NOT_EXIST_USER);
	}

	@Test
	@DisplayName("선착순 쿠폰 발급 [400] 같은 사용자가 쿠폰 재 발급 요청을 했을 때 (중복발급 X)")
	public void getCouponErrorWhenDuplicateIssued() {
		post(POST_ISSUED_COUPON,userId); // 정상
		Response response = post(POST_ISSUED_COUPON, userId);// 에러
		verifyApiResponseError(response.then(), DUPLICATE_COUPON_CLAIM);
	}
	@Test
	@DisplayName("선착순 쿠폰 발급 [400] 쿠폰 재고가 없을 때")
	public void getCouponErrorWhenCouponSoldOut() {
		long userId = 1L;
		for(int i =0; i<10; i++) post(POST_ISSUED_COUPON,userId++); // 정상
		Response response = post(POST_ISSUED_COUPON, ++userId);// 에러
		verifyApiResponseError(response.then(), COUPON_SOLD_OUT);
	}
}
