package kr.hhplus.be.server.interfaces.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.point.PointCommand;
import kr.hhplus.be.server.application.point.PointService;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.user.UserService;
import kr.hhplus.be.server.domain.product.Category;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@RestController
@RequestMapping("/api/test")
@Tag(name = "테스트", description = "부하테스트를 위한 API")
@RequiredArgsConstructor
public class TestController {
	private final UserService userService;
	private final ProductService productService;
	private final PointService pointService;

	@PostMapping("/charge-all")
	public ResponseEntity<ResponseApi<?>> chargeAll(
		@RequestParam BigDecimal price
	) {
		List<Long> userIds = userService.getAll().stream().map(User::getId).toList();
		userIds.forEach(userId -> {
			pointService.charge(PointCommand.Charge.of(userId, price));
		});
		return ResponseEntity.ok(ResponseApi.of(userIds));
	}

	@GetMapping("/products")
	public ResponseEntity<ResponseApi<List<Long>>> getTestProducts() {
		List<Long> productIds = new ArrayList<>();
		IntStream.range(0, 100).forEach(i -> {
			Product product = Product.builder()
				.productName("TEST Product Name" + i)
				.stock(10000)
				.category(Category.random().toString())
				.description("Test Product Description")
				.price(BigDecimal.valueOf((int)(Math.random() * (10000 - 5000 + 1)) + 5000))
				.build();
			productIds.add(productService.save(product).getId());
		});

		return ResponseEntity.ok(ResponseApi.of(productIds));
	}
}
