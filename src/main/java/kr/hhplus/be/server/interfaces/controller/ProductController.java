package kr.hhplus.be.server.interfaces.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.application.product.ProductCommand;
import kr.hhplus.be.server.application.product.ProductService;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.interfaces.dto.ProductDTO;
import kr.hhplus.be.server.support.config.swagger.SwaggerErrorExample;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
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
	public ResponseEntity<ResponseApi<List<ProductDTO.ProductResponse>>> searchFilter(@ModelAttribute ProductDTO.SearchRequest requestDTO) {
		List<ProductDTO.ProductResponse> products = productService.searchFilter(requestDTO.toCommand())
			.stream().map(ProductDTO.ProductResponse::from).toList();
		return ResponseEntity.ok(ResponseApi.of(products));
	}

	@GetMapping("/popular")
	@Operation(description = "인기 상품 조회 - 날짜 지정 판매량 많은 순 (default 최근 3일 5개)")
	public ResponseEntity<ResponseApi<List<ProductDTO.ProductResponse>>> findPopularAll(
		@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime startDate,
		@RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDateTime endDate,
		@PageableDefault(size = 5, page = 0) Pageable pageable
	) {
		ProductCommand.TopSelling command = new ProductCommand.TopSelling(startDate, endDate, pageable);
		List<ProductDTO.ProductResponse> products = productService.findPopularAll(command)
			.stream().map(ProductDTO.ProductResponse::from).toList();
		return ResponseEntity.ok(ResponseApi.of(products));
	}

	@PostMapping
	public ResponseEntity<ResponseApi<ProductDTO.ProductResponse>> save(@RequestBody ProductDTO.CreateRequest requestDTO){
		Product product = Product.builder()
			.productName(requestDTO.getProductName())
			.category(requestDTO.getCategory())
			.description(requestDTO.getDescription())
			.price(requestDTO.getPrice())
			.stock(requestDTO.getStock())
			.build();
		productService.save(product);
		return ResponseEntity.ok(ResponseApi.of(ProductDTO.ProductResponse.from(product)));
	}
}
