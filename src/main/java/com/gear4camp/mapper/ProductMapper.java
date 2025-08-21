package com.gear4camp.mapper;

import com.gear4camp.domain.Product;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ProductMapper {

    // 새 상품 등록
    void insertProduct(Product product);

    // 특정 상품 상세 조회
    Product selectProductById(Long id);

    // 상품 목록 전체 조회
    List<Product> selectAllProducts();

    // 상품 가격 조회
    Long findPriceById(Long productId);
    
    // 상품 정보 수정
    void updateProduct(Product product);

    // 상품 삭제
    void deleteProduct(Long id);
}