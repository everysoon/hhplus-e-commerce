package kr.hhplus.be.server.utils;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.swagger.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.function.Executable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class TestUtils {
	public static final String SUCCESS_MSG = "요청이 성공했습니다.";
	public static final String SUCCESS_PROCESS_CODE = "1";

	// default value
	public static final BigDecimal defaultPoint = BigDecimal.ZERO;
	public static final Integer defaultQuantity = 10;
	public static final String defaultCouponDesc = "TEST COUPON";
	public static final BigDecimal defaultCouponDiscount = convertToBigDecimal(1000);
	public static final BigDecimal defaultProductPrice = convertToBigDecimal(5000);
	public static final String defaultProductName = "다이소 장난감 ";
	public static final String defaultProductDesc = "유아용 다이소 자동차 장난감 ";
	public static final Integer defaultCouponRemainingStock = 5;
	public static final Integer defaultProductStock = 10;

	public static final Long productId = 1L;
	// default user info
	public static final Long invalidId = -1L;
	public static final BigDecimal chargePoint = convertToBigDecimal(100);
	public static final Long userId = 1L;
	public static final String userName = "minsoon";
	public static final String email = "soonforjoy@gmail.com";
	public static final String address = "Guro,Seoul";

	private static final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

	public static void verifyApiResponseSuccess(ValidatableResponse response) {
		response
			.statusCode(200) // HTTP 200 확인
			.body("success", equalTo(true)) // API 요청 성공 여부
			.body("message", equalTo(SUCCESS_MSG)) // 기본 메시지 확인
			.body("processCode", equalTo(SUCCESS_PROCESS_CODE)) // 성공 코드 확인
			.body("data", notNullValue()); // 데이터가 null이 아닌지 확인
	}

	public static void verifyApiResponseError(ValidatableResponse response, ErrorCode errorCode) {
		response
			.statusCode(errorCode.getStatusCode()) // HTTP 200 확인
			.body("success", equalTo(false)) // API 요청 성공 여부
			.body("message", equalTo(errorCode.getMessage())) // 기본 메시지 확인
			.body("processCode", equalTo(errorCode.getProcessCode())); // 성공 코드 확인
	}

//	public static <T> T parseResponse(Response response, Class<T> clazz) {
//		try {
//			String json = response.getBody().asString();
//			ResponseApi<T> responseApi = objectMapper.readValue(json, new TypeReference<ResponseApi<T>>() {
//			});
//			log.info("### parseResponse : {}", responseApi.getData());
//			return objectMapper.convertValue(responseApi.getData(), clazz);
//		} catch (Exception e) {
//			throw new RuntimeException("응답 파싱 오류", e);
//		}
//	}
	public static <T> T parseResponse(Response response, TypeReference<ResponseApi<T>> typeReference) {
		try {
			String json = response.getBody().asString();
			ResponseApi<T> responseApi = objectMapper.readValue(json, typeReference);

			log.info("### parseResponse : {}", responseApi.getData());

			return responseApi.getData();
		} catch (Exception e) {
			throw new RuntimeException("응답 파싱 오류", e);
		}
	}
	public static BigDecimal convertToBigDecimal(Integer number) {
		return BigDecimal.valueOf(number);
	}

	public static void assertThrowsIllegalArgument(Executable request) {
		assertThrows(IllegalArgumentException.class, request);
	}

	public static LocalDateTime now() {
		return LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES);
	}
}
