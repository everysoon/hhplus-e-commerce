package kr.hhplus.be.server.mock.e2e.user;

import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.response.Response;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.BaseE2ETest;
import kr.hhplus.be.server.dto.product.ProductResponseDTO;
import kr.hhplus.be.server.dto.user.UserCouponResponseDTO;
import kr.hhplus.be.server.dto.user.UserResponseDTO;
import kr.hhplus.be.server.enums.Category;
import kr.hhplus.be.server.enums.CouponType;
import kr.hhplus.be.server.enums.ProductStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static kr.hhplus.be.server.config.swagger.ErrorCode.*;
import static kr.hhplus.be.server.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;

public class MockProductE2ETest extends BaseE2ETest {
	private final String GET_POPULAR_PRODUCT = "/products/popular";
	private final String GET_PRODUCT_BY_ID = "/products/{productId}";

	@Test
	@DisplayName("상품 조회 [200] 상품 ID로 상품 조회")
	public void getProduct() {
		Response response = get(GET_PRODUCT_BY_ID, productId);
		verifyApiResponseSuccess(response.then());
		ProductResponseDTO productResponseDTO = parseResponse(response, new TypeReference<ResponseApi<ProductResponseDTO>>() {
		});
		// dto값 검증
		assertThat(productResponseDTO.getId()).isEqualTo(productId);
		assertThat(productResponseDTO.getProductName()).isEqualTo(defaultProductName + productId);
		assertThat(productResponseDTO.getStock()).isEqualTo(defaultProductStock);
		assertThat(productResponseDTO.getCategory()).isEqualTo(Category.BABY);
		assertThat(productResponseDTO.getDescription()).isEqualTo(defaultProductDesc + productId);
		assertThat(productResponseDTO.getPrice()).isEqualTo(defaultProductPrice);
		assertThat(productResponseDTO.getStatus()).isEqualTo(ProductStatus.AVAILABLE);
	}

	@Test
	@DisplayName("상품 조회 [400] 유효하지 않은 상품 ID로 상품 조회할 경우")
	public void getProductErrorWhenInvalidProductId() {
		Response response = get(GET_PRODUCT_BY_ID, invalidId);
		verifyApiResponseError(response.then(), NOT_EXIST_PRODUCT);
	}

	@Test
	@DisplayName("유저 쿠폰 조회 [200] 유저 쿠폰 조회")
	public void getUserCoupon() {
		Response response = get(GET_POPULAR_PRODUCT);
		verifyApiResponseSuccess(response.then());
		List<ProductResponseDTO> ProductResponseDTO = parseResponse(response, new TypeReference<ResponseApi<List<ProductResponseDTO>>>() {
		});
		// dto값 검증
//		assertThat(userCouponResponseDTO.getUserId()).isEqualTo(userId);
//		assertThat(userCouponResponseDTO.getCouponId()).isNotNull();
//		assertThat(userCouponResponseDTO.getCouponType()).isEqualTo(CouponType.FIXED);
//		assertThat(userCouponResponseDTO.getDescription()).isEqualTo(defaultCouponDesc);
//		assertThat(userCouponResponseDTO.getRemainingStock()).isEqualTo(defaultCouponRemainingStock);
//		assertThat(userCouponResponseDTO.getIssuedAt()).isEqualTo(now());
	}
}
