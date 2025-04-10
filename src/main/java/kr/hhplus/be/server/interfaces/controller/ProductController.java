package kr.hhplus.be.server.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.product.ProductSearchCommand;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.interfaces.dto.ProductDTO;
import kr.hhplus.be.server.support.config.swagger.SwaggerErrorExample;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static kr.hhplus.be.server.support.config.swagger.ErrorCode.NOT_EXIST_PRODUCT;

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
	public ResponseEntity<ResponseApi<ProductDTO.ProductResponse>> findProductById(
		@Parameter(description = "상품 ID", required = true)
		@PathVariable Long productId
	) {
		Product product = productService.findById(productId);
		return ResponseEntity.ok(ResponseApi.of(ProductDTO.ProductResponse.from(product)));
	}

	@GetMapping
	@Operation(description = "상품 필터링 목록 조회")
	public ResponseEntity<ResponseApi<List<ProductDTO.ProductResponse>>> findAll(@ModelAttribute ProductSearchCommand command) {
		List<ProductDTO.ProductResponse> products = productService.findAll(command)
			.stream().map(ProductDTO.ProductResponse::from).toList();
		return ResponseEntity.ok(ResponseApi.of(products));
	}

	@GetMapping("/popular")
	@Operation(description = "인기 상품 조회 - 최근 3일간 판매량 많은 순")
	public ResponseEntity<ResponseApi<List<ProductDTO.ProductResponse>>> findPopularAll() {
		List<ProductDTO.ProductResponse> products = productService.findPopularAll()
			.stream().map(ProductDTO.ProductResponse::from).toList();
		return ResponseEntity.ok(ResponseApi.of(products));
	}
}
