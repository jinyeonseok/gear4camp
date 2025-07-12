package com.gear4camp.controller;

import com.gear4camp.domain.Product;
import com.gear4camp.dto.product.ProductRequestDto;
import com.gear4camp.dto.product.ProductResponseDto;
import com.gear4camp.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // 상품 등록
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "상품 등록", description = "신규 상품을 등록합니다. 인증된 사용자만 가능합니다.")
    @PostMapping
    public ResponseEntity<Void> createProduct(@Valid @RequestBody ProductRequestDto dto, Authentication authentication) {
        String userId = authentication.getName(); // JwtAuthenticationFilter에서 넣어준 userId
        productService.createProduct(dto, userId);
        return ResponseEntity.ok().build();
    }

    // 특정 상품 조회
    @Operation(summary = "상품 상세 조회", description = "상품 ID를 기준으로 상품 상세 정보를 조회합니다.")
    @Parameter(name = "id", description = "조회할 상품 ID", required = true)
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getProduct(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(productService.toDto(product));
    }

    // 전체 상품 목록 조회
    @Operation(summary = "전체 상품 목록 조회", description = "등록된 모든 상품을 리스트로 반환합니다.")
    @GetMapping
    public ResponseEntity<List<ProductResponseDto>> getAllProducts() {
        List<Product> products = productService.getAllProducts();
        return ResponseEntity.ok(productService.toDtoList(products));
    }

    // 상품 수정
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "상품 수정", description = "상품 ID에 해당하는 상품을 수정합니다. 등록자만 수정할 수 있습니다.")
    @Parameters({
            @Parameter(name = "id", description = "수정할 상품 ID", required = true)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Void> updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto dto, Authentication authentication) {
        String userId = authentication.getName();
        productService.updateProduct(id, dto, userId);
        return ResponseEntity.ok().build();
    }


    // 상품 삭제
    @SecurityRequirement(name = "bearerAuth")
    @Operation(summary = "상품 삭제", description = "상품 ID에 해당하는 상품을 삭제합니다. 등록자만 삭제할 수 있습니다.")
    @Parameter(name = "id", description = "삭제할 상품 ID", required = true)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id, Authentication authentication) {
        String userId = authentication.getName();
        productService.deleteProduct(id, userId);
        return ResponseEntity.ok().build();
    }
}