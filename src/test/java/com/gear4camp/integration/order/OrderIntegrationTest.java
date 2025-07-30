package com.gear4camp.integration.order;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gear4camp.dto.cart.CartRequestDto;
import com.gear4camp.integration.common.TestAuthUtils;
import com.gear4camp.integration.common.TestAuthUtils.TokenAndUserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestAuthUtils testAuthUtils;

    @Test
    @DisplayName("주문 생성 → 조회 → 취소 흐름 테스트")
    void createOrder_thenRetrieveAndCancel() throws Exception {

        TokenAndUserId user = testAuthUtils.registerAndLoginRandomWithUserId();
        String token = user.getToken();

        Long productId = 1L;

        // 1. 장바구니에 상품 담기
        CartRequestDto cartDto = new CartRequestDto();
        cartDto.setProductId(productId);
        cartDto.setQuantity(1);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(cartDto)))
                .andExpect(status().isCreated());

        // 2. 주문 생성
        MvcResult orderCreateResult = mockMvc.perform(post("/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isCreated())
                .andReturn();

        String orderJson = orderCreateResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode orderNode = objectMapper.readTree(orderJson);
        Long orderId = orderNode.get("id").asLong();

        // 3. 주문 단건 조회
        MvcResult detailResult = mockMvc.perform(get("/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String detailJson = detailResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode detailNode = objectMapper.readTree(detailJson);
        assertThat(detailNode.get("id").asLong()).isEqualTo(orderId);

        // 4. 주문 취소
        mockMvc.perform(post("/orders/" + orderId + "/cancel")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // 5. 취소된 주문 상태 확인
        MvcResult cancelledResult = mockMvc.perform(get("/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String cancelledJson = cancelledResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode cancelledNode = objectMapper.readTree(cancelledJson);
        String statusValue = cancelledNode.get("status").asText();
        assertThat(statusValue).isEqualTo("CANCELLED");

    }

}