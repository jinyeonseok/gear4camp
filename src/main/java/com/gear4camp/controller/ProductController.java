package com.gear4camp.controller;

import com.gear4camp.domain.Product;
import com.gear4camp.service.ProductService;
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
    public void createProduct(@RequestBody Product product) {
        productService.createProduct(product);
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
    public void updateProduct(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        productService.updateProduct(product);
    }

    // 상품 삭제
    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
}