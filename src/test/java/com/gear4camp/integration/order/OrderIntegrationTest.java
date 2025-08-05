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
import java.util.List;

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

        // 2. 장바구니 목록 조회
        MvcResult getCartResult = mockMvc.perform(get("/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String getCartBody = getCartResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode getCartNode = objectMapper.readTree(getCartBody);

        Long cartId = getCartNode.get("cartList").get(0).get("id").asLong();

        // 3. 주문 생성 요청
        List<Long> cartIdList = List.of(cartId);

        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(cartIdList)))
                .andExpect(status().isCreated());

        MvcResult getOrderResult = mockMvc.perform(get("/orders")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String orderJson = getOrderResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode orderNode = objectMapper.readTree(orderJson);
        Long orderId = orderNode.get("orderList").get(0).get("id").asLong();

        // 3. 주문 단건 조회
        MvcResult detailResult = mockMvc.perform(get("/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String detailJson = detailResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode detailNode = objectMapper.readTree(detailJson).get("orderDto");
        assertThat(detailNode.get("id").asLong()).isEqualTo(orderId);

        // 4. 주문 취소
        mockMvc.perform(put("/orders/" + orderId + "/cancel")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk());

        // 5. 취소된 주문 상태 확인
        MvcResult cancelledResult = mockMvc.perform(get("/orders/" + orderId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String cancelledJson = cancelledResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode cancelledNode = objectMapper.readTree(cancelledJson).get("orderDto");
        String statusValue = cancelledNode.get("status").asText();
        assertThat(statusValue).isEqualTo("CANCELLED");

    }

}