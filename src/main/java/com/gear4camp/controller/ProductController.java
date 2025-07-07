package com.gear4camp.controller;

import com.gear4camp.domain.Product;
import com.gear4camp.dto.product.ProductRequestDto;
import com.gear4camp.service.ProductService;
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
    @PostMapping
    public ResponseEntity<Void> createProduct(@RequestBody ProductRequestDto dto, Authentication authentication) {
        // jwt 토큰에서 인증된 사용자 id 가져오기
        String userId = authentication.getName(); // JwtAuthenticationFilter에서 넣어준 userId
        productService.createProduct(dto, userId);
        return ResponseEntity.ok().build();
    }

    // 특정 상품 조회
    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    // 전체 상품 목록 조회
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    // 상품 수정
    @PutMapping("/{id}")
    public void updateProduct(@PathVariable Long id, @RequestBody ProductRequestDto dto, Authentication authentication) {
        String userId = authentication.getName();
        System.out.println("userId : " + userId);
        productService.updateProduct(id, dto, userId);
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id, Authentication authentication) {
        String userId = authentication.getName();
        productService.deleteProduct(id, userId);
    }
}