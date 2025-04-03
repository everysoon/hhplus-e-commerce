package kr.hhplus.be.server.controller;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import kr.hhplus.be.server.ResponseApi;
import kr.hhplus.be.server.config.swagger.SwaggerErrorExample;
import kr.hhplus.be.server.config.swagger.SwaggerSuccessExample;
import kr.hhplus.be.server.dto.product.ProductResponseDTO;
import kr.hhplus.be.server.service.MockService;
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

    private final MockService mockService;

    @GetMapping("/{productId}")
    @SwaggerSuccessExample(responseType = ProductResponseDTO.class)
    @SwaggerErrorExample({
        NOT_EXIST_PRODUCT
    })
    public ResponseEntity<ResponseApi<ProductResponseDTO>> getUserPoint(
        @Parameter(description = "상품 ID", required = true)
        @PathVariable Long productId
    ) {
        return ResponseEntity.ok(mockService.findProductById(productId));
    }

    @GetMapping("/popular")
    @SwaggerSuccessExample(responseType = ProductResponseDTO.class)
    public ResponseEntity<ResponseApi<List<ProductResponseDTO>>> findAllPopularProducts() {
        return ResponseEntity.ok(mockService.findAllPopularProducts());
    }
}
