package kr.hhplus.be.server.mock.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.BaseE2ETest;
import kr.hhplus.be.server.dto.order.OrderProductRequestDTO;
import kr.hhplus.be.server.dto.order.OrderRequestDTO;
import kr.hhplus.be.server.dto.order.OrderResponseDTO;
import kr.hhplus.be.server.enums.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.IntStream;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.post;
import static kr.hhplus.be.server.config.swagger.ErrorCode.*;
import static kr.hhplus.be.server.utils.TestUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MockOrderE2ETest extends BaseE2ETest {
	private final String POST_ORDER = "/orders";

	public List<OrderProductRequestDTO> createProducts() {
		return List.of(new OrderProductRequestDTO(productId, defaultQuantity));
	}

	public OrderRequestDTO createInvalidProductsOrderRequestDTO() {
		return OrderRequestDTO.builder()
			.userId(pointAvailableUserId)
			.couponId(List.of(availableCouponId))
			.products(List.of())
			.build();
	}

	public Response requestOrder(OrderRequestDTO dto) {
		return given()
			.contentType(ContentType.JSON)
			.body(dto)
			.when().post(POST_ORDER);
	}

	public OrderRequestDTO createAvailableOrderRequestDTO(Long userId) {
		return OrderRequestDTO.builder()
			.userId(userId)
			.couponId(List.of(availableCouponId))
			.products(createProducts())
			.build();
	}

	public OrderRequestDTO createOrderRequestDTO(Long userId, List<UUID> couponIds, List<OrderProductRequestDTO> products) {
		return OrderRequestDTO.builder()
			.userId(userId)
			.couponId(couponIds)
			.products(products)
			.build();
	}

	@Test
	@DisplayName("상품 주문 [200] 주문 정상 완료 - 쿠폰 x")
	public void order() {
		OrderRequestDTO requestDTO = OrderRequestDTO.builder()
			.userId(pointAvailableUserId)
			.couponId(null)
			.products(createProducts())
			.build();

		Response response = requestOrder(requestDTO);

		verifyApiResponseSuccess(response.then());

		OrderResponseDTO order = parseResponse(response, new TypeReference<ResponseApi<OrderResponseDTO>>() {
		});
		// dto값 검증

		assertThat(order.getUserId()).isEqualTo(pointAvailableUserId);
		assertThat(order.getOrderId()).isNotNull();
		assertThat(order.getOrderInfo()).isNotNull();
		assertThat(order.getPaymentMethod()).isEqualTo(PaymentMethod.POINTS);
		assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
		assertThat(order.getCouponDiscountAmount()).isEqualTo(convertToBigDecimal(0));
		assertThat(order.getTotalPrice()).isEqualTo(convertToBigDecimal(5000));
		assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDERED);
		assertThat(order.getOrderedAt()).isEqualTo(now());
	}

	@Test
	@DisplayName("상품 주문 [200] 주문 정상 완료 - 쿠폰 o")
	public void getCoupon() {
		OrderRequestDTO requestDTO = createAvailableOrderRequestDTO(pointAvailableUserId);
		Response response = requestOrder(requestDTO);
		verifyApiResponseSuccess(response.then());

		OrderResponseDTO order = parseResponse(response, new TypeReference<ResponseApi<OrderResponseDTO>>() {
		});
		// dto값 검증
		assertThat(order.getUserId()).isEqualTo(pointAvailableUserId);
		assertThat(order.getOrderId()).isNotNull();
		assertThat(order.getOrderInfo()).isNotNull();
		assertThat(order.getPaymentMethod()).isEqualTo(PaymentMethod.POINTS);
		assertThat(order.getPaymentStatus()).isEqualTo(PaymentStatus.COMPLETED);
		assertThat(order.getCouponDiscountAmount()).isEqualTo(convertToBigDecimal(1000));
		assertThat(order.getTotalPrice()).isEqualTo(convertToBigDecimal(4000));
		assertThat(order.getStatus()).isEqualTo(OrderStatus.ORDERED);
		assertThat(order.getOrderedAt()).isEqualTo(now());
	}

	@Test
	@DisplayName("상품 주문 [400] 유효한 유저ID가 아닐 때")
	public void getCouponErrorWhenInvalidUserId() {
		OrderRequestDTO requestDTO = createAvailableOrderRequestDTO(invalidId);
		Response response = requestOrder(requestDTO);
		verifyApiResponseError(response.then(), NOT_EXIST_USER);
	}

	@Test
	@DisplayName("상품 주문 [400] 해당 쿠폰이 유저 권한이 아닐 때")
	public void getCouponErrorWhenInvalidUserIdAndCoupon() {
		OrderRequestDTO requestDTO = createAvailableOrderRequestDTO(userId);
		Response response = requestOrder(requestDTO);
		verifyApiResponseError(response.then(), INVALID_USER_COUPON);
	}

	@Test
	@DisplayName("상품 주문 [400] 주문 상품 리스트가 없을 때")
	public void getCouponErrorWhenInvalidProductId() {
		OrderRequestDTO requestDTO = createInvalidProductsOrderRequestDTO();
		Response response = requestOrder(requestDTO);
		verifyApiResponseError(response.then(), NOT_EXIST_ORDER_ITEM);
	}

	@Test
	@DisplayName("상품 주문 [400] 쿠폰이 만료되거나 사용할 수 없는 상태일 때")
	public void getCouponErrorWhenExpiredCoupon() {
		List<UUID> uuids = List.of(UUID.fromString("0cf78826-f30f-4121-9476-10f6ac1f6e7f"));
		OrderRequestDTO requestDTO = createOrderRequestDTO(pointAvailableUserId, uuids, createProducts());
		Response response = requestOrder(requestDTO);
		verifyApiResponseError(response.then(), INVALID_COUPON);
	}

	@Test
	@DisplayName("상품 주문 [400] 등록된 쿠폰 번호가 아닐 때")
	public void getCouponErrorWhenInvalidCoupon() {
		List<UUID> uuids = List.of(UUID.randomUUID());
		OrderRequestDTO requestDTO = createOrderRequestDTO(pointAvailableUserId, uuids, createProducts());
		Response response = requestOrder(requestDTO);
		verifyApiResponseError(response.then(), NOT_EXIST_COUPON);
	}

	@Test
	@DisplayName("상품 주문 [400] 락 획득에 실패했을 때")
	public void getCouponErrorWhenAcquireLock() throws ExecutionException, InterruptedException {

		ExecutorService executor = Executors.newFixedThreadPool(2);
		CountDownLatch latch = new CountDownLatch(1);
		List<Future<Response>> futures = IntStream.range(0, 2)
			.mapToObj(i -> executor.submit(() -> {
				latch.await();
				return requestOrder(createAvailableOrderRequestDTO(pointAvailableUserId));
			}))
			.toList();
		latch.countDown(); // 모든 스레드가 동시에 실행되도록 트리거

		// 두 번째 요청이 락 획득 실패
		verifyApiResponseError(futures.get(1).get().then(), LOCK_ACQUISITION_FAIL);

		executor.shutdown();
	}

	@Test
	@DisplayName("상품 주문 [400] 주문한 상품의 재고가 없을 때")
	public void getCouponErrorWhenProductSoldOut() {
		OrderRequestDTO requestDTO = createOrderRequestDTO(pointAvailableUserId, List.of(), createProducts());
		IntStream.range(0, 6).forEach((index) -> requestOrder(requestDTO)); // 정상
		Response response = requestOrder(requestDTO);
		verifyApiResponseError(response.then(), OUT_OF_STOCK);
	}
}
