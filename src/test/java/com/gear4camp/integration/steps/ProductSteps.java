package com.gear4camp.integration.steps;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gear4camp.dto.product.ProductRequestDto;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProductSteps {
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;

    public ProductSteps(MockMvc mockMvc, ObjectMapper objectMapper) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
    }

    public long createProduct(String token, String name, long price) throws Exception {
        ProductRequestDto dto = new ProductRequestDto();
        dto.setName(name);
        dto.setDescription("테스트 상품 설명");
        dto.setPrice(price);
        dto.setStock(5);
        dto.setThumbnailUrl("https://example.com/test.jpg");

        MvcResult res = mockMvc.perform(post("/products")
                        .header("Authorization", bearer(token))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andReturn();

        return objectMapper.readTree(res.getResponse().getContentAsString())
                .path("productId").asLong(); // 응답 JSON 구조에 맞게 수정
    }

    // 단일 책임 및 진입점 통제
    // 호출부가 많이질수록 실수 확률이 높아지기에 호출부에서는 간단히 처리
    private String bearer(String t) {
        return t.startsWith("Bearer ") ? t : "Bearer " + t;
    }
}