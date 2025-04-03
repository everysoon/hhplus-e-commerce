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
import static org.hamcrest.Matchers.*;

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

	/**
	 * 고민 : 리스트 response가 반환 될 때는 안에 값을 모두 검증을 할까 ?
	 * 해결 : 1. 데이터가 정상적으로 반환되는지 검증할 때 -> 리스트 크기/empty 만 검증
	 * 2. 응답 데이터 구조만 확인할 때 -> 첫번째 요소의 값들만 모두 검증
	 * 3. 모든 요소가 "특정 조건"을 만족하는지 확인할 때 -> .allSatisfy() 사용
	 * 4. 각 요소를 개별적으로 "세부 검증"할 때 -> 반복문(forEach 또는 for문)을 이용한 검증
	 * 대부분의 e2e 테스트에서는 2번의 경우로 첫번째 요소의 값들만 검증.
	 */
	@Test
	@DisplayName("유저 쿠폰 조회 [200] 유저 쿠폰 조회")
	public void getUserCoupon() {
		Response response = get(GET_POPULAR_PRODUCT);

		verifyApiResponseSuccess(response.then())
			.body("data", not(empty()))
			.body("data.size()", equalTo(3));

		List<ProductResponseDTO> list = parseResponse(response, new TypeReference<ResponseApi<List<ProductResponseDTO>>>() {
		});
		// dto값 검증
		assertThat(list)
			.allSatisfy(product -> {
				assertThat(product.getId()).isNotNull();
				assertThat(product.getProductName()).contains(defaultProductName);
				assertThat(product.getStock()).isEqualTo(defaultProductStock);
				assertThat(product.getCategory()).isEqualTo(Category.BABY);
				assertThat(product.getDescription()).contains(defaultProductDesc);
				assertThat(product.getPrice()).isEqualTo(defaultProductPrice);
				assertThat(product.getStatus()).isEqualTo(ProductStatus.AVAILABLE);
			});

		assertThat(list.get(0).getId()).isEqualTo(productId);
		assertThat(list.get(0).getProductName()).contains(defaultProductName + productId);
		assertThat(list.get(0).getStock()).isEqualTo(defaultProductStock);
		assertThat(list.get(0).getCategory()).isEqualTo(Category.BABY);
		assertThat(list.get(0).getDescription()).contains(defaultProductDesc + productId);
		assertThat(list.get(0).getPrice()).isEqualTo(defaultProductPrice);
		assertThat(list.get(0).getStatus()).isEqualTo(ProductStatus.AVAILABLE);
	}
}
