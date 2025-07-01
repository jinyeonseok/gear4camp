package com.gear4camp.service;

import com.gear4camp.domain.Product;
import com.gear4camp.domain.User;
import com.gear4camp.dto.product.ProductRequestDto;
import com.gear4camp.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductMapper productMapper;
    private final UserService userService;

    public void createProduct(ProductRequestDto dto, String userId) {
        User user = userService.getByUserId(userId); // DB에서 사용자 확인 + 예외 처리

        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setThumbnailUrl(dto.getThumbnailUrl());
        product.setCreatedBy(user.getId());

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