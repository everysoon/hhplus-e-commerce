package kr.hhplus.be.server.controller;

import static kr.hhplus.be.server.config.swagger.ErrorCode.NOT_EXIST_PRODUCT;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.application.product.dto.ProductResponseDTO;
import kr.hhplus.be.server.config.swagger.SwaggerErrorExample;
import kr.hhplus.be.server.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
		Product product = productService.findById(productId);
		return ResponseEntity.ok(ResponseApi.of(ProductResponseDTO.from(product)));
	}

	@GetMapping
	@Operation(description = "상품 필터링 목록 조회")
	public ResponseEntity<ResponseApi<List<ProductResponseDTO>>> findAll() {
		List<ProductResponseDTO> products = productService.findAll()
			.stream().map(ProductResponseDTO::from).toList();
		return ResponseEntity.ok(ResponseApi.of(products));
	}

	@GetMapping("/popular")
	@Operation(description = "인기 상품 조회 - ")
	public ResponseEntity<ResponseApi<List<ProductResponseDTO>>> findAllPopularProducts() {
		List<ProductResponseDTO> products = productService.findAllPopularProducts()
			.stream().map(ProductResponseDTO::from).toList();
		return ResponseEntity.ok(ResponseApi.of(products));
	}
}
