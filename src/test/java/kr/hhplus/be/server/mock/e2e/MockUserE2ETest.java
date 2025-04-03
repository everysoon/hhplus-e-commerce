package kr.hhplus.be.server.mock.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.response.Response;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.BaseE2ETest;
import kr.hhplus.be.server.dto.user.UserCouponResponseDTO;
import kr.hhplus.be.server.dto.user.UserResponseDTO;
import kr.hhplus.be.server.enums.CouponType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static kr.hhplus.be.server.config.swagger.ErrorCode.*;
import static kr.hhplus.be.server.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MockUserE2ETest extends BaseE2ETest {
	private final String GET_USER_COUPON = "/users/{userId}/coupon";
	private final String GET_USER_POINT = "/users/{userId}/point";
	private final String POST_USER_POINT = "/users/{userId}/point";

	@Test
	@DisplayName("유저 포인트 조회 [200] 유저 포인트 조회")
	public void getUserPoint() {
		Response response = get(GET_USER_POINT, userId);
		verifyApiResponseSuccess(response.then());
		UserResponseDTO userResponseDTO = parseResponse(response, new TypeReference<ResponseApi<UserResponseDTO>>() {
		});
		// dto값 검증
		assertThat(userResponseDTO.getPoint()).isEqualTo(defaultPoint);
		assertThat(userResponseDTO.getName()).isEqualTo(userName);
		assertThat(userResponseDTO.getId()).isEqualTo(userId);
		assertThat(userResponseDTO.getEmail()).isEqualTo(email);
		assertThat(userResponseDTO.getAddress()).isEqualTo(address);
	}

	@Test
	@DisplayName("유저 포인트 조회 [400] 유효하지 않은 유저 ID로 유저 포인트 조회할 경우")
	public void getUserPointErrorWhenInvalidId() {
		Response response = get(GET_USER_POINT, invalidId);
		verifyApiResponseError(response.then(), NOT_EXIST_USER);
	}

	@Test
	@DisplayName("유저 포인트 조회 [400] 유저 ID값을 넣지 않았을 경우")
	public void getUserPointErrorWhenNullUserId() {
		// 이 테스트가 꼭 필요할까 ?
		assertThrowsIllegalArgument(
			() -> get(GET_USER_POINT)
		);
	}

	@Test
	@DisplayName("유저 쿠폰 조회 [200] 유저 쿠폰 조회")
	public void getUserCoupon() {
		Response response = get(GET_USER_COUPON, userId);
		verifyApiResponseSuccess(response.then());
		UserCouponResponseDTO userCouponResponseDTO = parseResponse(response, new TypeReference<ResponseApi<UserCouponResponseDTO>>() {
		});
		// dto값 검증
		assertThat(userCouponResponseDTO.getUserId()).isEqualTo(userId);
		assertThat(userCouponResponseDTO.getCouponId()).isNotNull();
		assertThat(userCouponResponseDTO.getCouponType()).isEqualTo(CouponType.FIXED);
		assertThat(userCouponResponseDTO.getDescription()).isEqualTo(defaultCouponDesc);
		assertThat(userCouponResponseDTO.getRemainingStock()).isEqualTo(defaultCouponRemainingStock);
		assertThat(userCouponResponseDTO.getIssuedAt()).isEqualTo(now());
	}

	@Test
	@DisplayName("유저 쿠폰 조회 [400] 유효하지 않은 유저 ID로 유저 쿠폰 조회할 경우")
	public void getUserCouponErrorWhenInvalidId() {
		Response response = get(GET_USER_COUPON, invalidId);
		verifyApiResponseError(response.then(), NOT_EXIST_USER);
	}

	@Test
	@DisplayName("유저 쿠폰 조회 [400] 유저 ID값을 넣지 않았을 경우")
	public void getUserCouponErrorWhenNullUserId() {
		// 이 테스트가 꼭 필요할까 ?
		assertThrowsIllegalArgument(
			() -> get(GET_USER_COUPON)
		);
	}

	@Test
	@DisplayName("포인트 충전 [200] 유저 포인트 충전")
	public void chargePoint() {
		Response response = given()
			.pathParam("userId", userId)
			.queryParam("price", chargePoint)
			.when()
			.post(POST_USER_POINT);
		verifyApiResponseSuccess(response.then());
		UserResponseDTO userResponseDTO = parseResponse(response, new TypeReference<ResponseApi<UserResponseDTO>>() {
		});
		assertThat(userResponseDTO.getName()).isEqualTo(userName);
		assertThat(userResponseDTO.getId()).isEqualTo(userId);
		assertThat(userResponseDTO.getEmail()).isEqualTo(email);
		assertThat(userResponseDTO.getAddress()).isEqualTo(address);
		assertThat(userResponseDTO.getPoint()).isEqualTo(chargePoint);
	}

	@Test
	@DisplayName("포인트 충전 [400] 유저를 찾을 수 없는 경우")
	public void chargePointErrorWhenInvalidId() {
		Response response = given()
			.pathParam("userId", invalidId)
			.queryParam("price", chargePoint)
			.when()
			.post(POST_USER_POINT);
		verifyApiResponseError(response.then(), NOT_EXIST_USER);
	}

	@Test
	@DisplayName("포인트 충전 [400] 충전 금액이 0보다 작은경우")
	public void chargePointErrorWhenInvalidChargeAmount() {
		Response response = given()
			.pathParam("userId", userId)
			.queryParam("price", 0)
			.when()
			.post(POST_USER_POINT);
		verifyApiResponseError(response.then(), INVALID_CHARGE_AMOUNT);
	}
}
