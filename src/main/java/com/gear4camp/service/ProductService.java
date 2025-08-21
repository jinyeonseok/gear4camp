package com.gear4camp.service;

import com.gear4camp.domain.Product;
import com.gear4camp.domain.User;
import com.gear4camp.dto.product.ProductRequestDto;
import com.gear4camp.dto.product.ProductResponseDto;
import com.gear4camp.exception.CustomException;
import com.gear4camp.exception.ErrorCode;
import com.gear4camp.mapper.ProductMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public void updateProduct(Long productId, ProductRequestDto dto, String userId) {
        Product product = productMapper.selectProductById(productId);
        if (product == null) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        Long currentUserDbId = userService.getByUserId(userId).getId();
        if (!product.getCreatedBy().equals(currentUserDbId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        // 업데이트할 값 세팅
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStock(dto.getStock());
        product.setThumbnailUrl(dto.getThumbnailUrl());

        productMapper.updateProduct(product);
    }

    // 상품 삭제 처리
    public void deleteProduct(Long productId, String userId) {
        Product product = productMapper.selectProductById(productId);
        if (product == null) {
            throw new CustomException(ErrorCode.PRODUCT_NOT_FOUND);
        }

        Long currentUserDbId = userService.getByUserId(userId).getId();
        if (!product.getCreatedBy().equals(currentUserDbId)) {
            throw new CustomException(ErrorCode.FORBIDDEN);
        }

        productMapper.deleteProduct(productId);
    }

    public ProductResponseDto toDto(Product product) {
        ProductResponseDto dto = new ProductResponseDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setStock(product.getStock());
        dto.setThumbnailUrl(product.getThumbnailUrl());
        dto.setCreatedBy(product.getCreatedBy());
        dto.setCreatedAt(product.getCreatedAt().toString());  // LocalDateTime -> String
        dto.setUpdatedAt(product.getUpdatedAt().toString());  // LocalDateTime -> String
        return dto;
    }

    public List<ProductResponseDto> toDtoList(List<Product> productList) {
        return productList.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }
}