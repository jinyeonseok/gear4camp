package com.gear4camp.service;

import com.gear4camp.domain.Product;
import com.gear4camp.mapper.ProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductMapper productMapper;

    public ProductService(ProductMapper productMapper) {
        this.productMapper = productMapper;
    }

    // 상품 등록 요청 처리
    public void createProduct(Product product) {
        productMapper.insertProduct(product);
    }

    // 특정 상품 조회
    public Product getProductById(Long id) {
        return productMapper.selectProductById(id);
    }

    // 전체 상품 목록 조회
    public List<Product> getAllProducts() {
        return productMapper.selectAllProducts();
    }

    // 상품 정보 수정
    public void updateProduct(Product product) {
        productMapper.updateProduct(product);
    }

    // 상품 삭제 처리
    public void deleteProduct(Long id) {
        productMapper.deleteProduct(id);
    }
}