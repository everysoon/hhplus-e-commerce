package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.product.dto.ProductResponseDTO;
import kr.hhplus.be.server.config.swagger.SwaggerErrorExample;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static kr.hhplus.be.server.config.swagger.ErrorCode.*;

@RestController
@RequestMapping("/api/products")
@Tag(name = "상품", description = "상품 관련 API")
@RequiredArgsConstructor
public class ProductController {

	private final ProductService productService;

	@GetMapping("/{productId}")
	@Operation(description = "상품 ID로 해당 상품 조회")
	@SwaggerErrorExample({
		NOT_EXIST_PRODUCT
	})
	public ResponseEntity<ResponseApi<ProductResponseDTO>> findProductById(
		@Parameter(description = "상품 ID", required = true)
		@PathVariable Long productId
	) {
		return ResponseEntity.ok(ResponseApi.of(productService.findById(productId)));
	}

	@GetMapping
	@Operation(description = "상품 필터링 목록 조회")
	public ResponseEntity<ResponseApi<List<ProductResponseDTO>>> findAll() {
		return ResponseEntity.ok(ResponseApi.of(productService.findAll()));
	}

	@GetMapping("/popular")
	@Operation(description = "인기 상품 조회 - ")
	public ResponseEntity<ResponseApi<List<ProductResponseDTO>>> findAllPopularProducts() {
		return ResponseEntity.ok(ResponseApi.of(productService.findAllPopularProducts()));
	}
}
