package com.gear4camp.integration.cart;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gear4camp.dto.cart.CartQuantityUpdateRequest;
import com.gear4camp.dto.cart.CartRequestDto;
import com.gear4camp.dto.product.ProductRequestDto;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class CartIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TestAuthUtils testAuthUtils;

    @Test
    @DisplayName("장바구니 담기 성공")
    void addToCart_success() throws Exception {

        TokenAndUserId user = testAuthUtils.registerAndLoginRandomWithUserId();
        String token = user.getToken();

        Long productId = 1L;
        int quantity = 2;
        long price = 10000;

        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("캠핑 의자");
        productRequestDto.setDescription("접이식 방수 캠핑 의자");
        productRequestDto.setPrice(price);
        productRequestDto.setStock(2);
        productRequestDto.setThumbnailUrl("https://example.com/image.jpg");

        MvcResult createRes = mockMvc.perform(post("/products")
                                .contentType(MediaType.APPLICATION_JSON)
                                .header("Authorization", "Bearer " + token)
                                .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isCreated())
                        .andReturn();

        long createdId = objectMapper.readTree(createRes.getResponse().getContentAsString())
                .path("productId").asLong();

        CartRequestDto requestDto = new CartRequestDto();
        requestDto.setProductId(createdId);
        requestDto.setQuantity(quantity);
        requestDto.setPrice(price);

        MvcResult result = mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(responseBody)
                )
        );

        // 클라이언트가 원하는 응답구조로 응답값이 왔는데 확인하기위함
        assertThat(responseBody).contains("productId");
        assertThat(responseBody).contains("quantity");
    }

    @Test
    @DisplayName("장바구니 중복 담기 → 수량 증가")
    void addToCart_duplicateIncreasesQuantity() throws Exception {

        TokenAndUserId user = testAuthUtils.registerAndLoginRandomWithUserId();
        String token = user.getToken();

        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("캠핑 의자");
        productRequestDto.setDescription("접이식 방수 캠핑 의자");
        productRequestDto.setPrice(10000L);
        productRequestDto.setStock(2);
        productRequestDto.setThumbnailUrl("https://example.com/image.jpg");

        MvcResult createRes = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        long createdId = objectMapper.readTree(createRes.getResponse().getContentAsString())
                .path("productId").asLong();

        CartRequestDto requestDto = new CartRequestDto();
        requestDto.setProductId(createdId);
        requestDto.setQuantity(1);
        requestDto.setPrice(10000L);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        MvcResult postResult = mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        MvcResult getResult = mockMvc.perform(get("/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        // GET
        String getResponseBody = getResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode cartList = objectMapper.readTree(getResponseBody).get("cartList");
        int quantity = cartList.get(0).get("quantity").asInt();

        assertThat(quantity).isEqualTo(2);

        // POST
        String postResponseBody = postResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(postResponseBody)
                )
        );

    }

    @Test
    @DisplayName("장바구니 조회 - 성공")
    void getCart_success() throws Exception {
        TokenAndUserId user = testAuthUtils.registerAndLoginRandomWithUserId();
        String token = user.getToken();

        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("캠핑 의자");
        productRequestDto.setDescription("접이식 방수 캠핑 의자");
        productRequestDto.setPrice(10000L);
        productRequestDto.setStock(2);
        productRequestDto.setThumbnailUrl("https://example.com/image.jpg");

        MvcResult createRes = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        long createdId = objectMapper.readTree(createRes.getResponse().getContentAsString())
                .path("productId").asLong();

        CartRequestDto requestDto = new CartRequestDto();
        requestDto.setProductId(createdId);
        requestDto.setQuantity(3);
        requestDto.setPrice(10000L);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        MvcResult result = mockMvc.perform(get("/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);

        JsonNode cartList = objectMapper.readTree(responseBody).get("cartList");

        assertThat(cartList.get(0).get("productId").asLong()).isEqualTo(createdId);
        assertThat(cartList.get(0).get("quantity").asInt()).isEqualTo(3);

        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(responseBody)
                )
        );

    }

    @Test
    @DisplayName("장바구니 수량 수정")
    void updateCartQuantity_success() throws Exception {
        TokenAndUserId user = testAuthUtils.registerAndLoginRandomWithUserId();
        String token = user.getToken();

        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("캠핑 의자");
        productRequestDto.setDescription("접이식 방수 캠핑 의자");
        productRequestDto.setPrice(10000L);
        productRequestDto.setStock(2);
        productRequestDto.setThumbnailUrl("https://example.com/image.jpg");

        MvcResult createRes = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        long createdId = objectMapper.readTree(createRes.getResponse().getContentAsString())
                .path("productId").asLong();

        // 장바구니에 상품 담기
        CartRequestDto requestDto = new CartRequestDto();
        requestDto.setProductId(createdId);
        requestDto.setQuantity(1);
        requestDto.setPrice(10000L);

        mockMvc.perform(post("/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        // 장바구니 목록 조회 (cartId 확보)
        MvcResult result = mockMvc.perform(get("/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode json = objectMapper.readTree(responseBody);
        Long cartId = json.get("cartList").get(0).get("id").asLong();

        CartQuantityUpdateRequest dto = new CartQuantityUpdateRequest();
        dto.setQuantity(5);

        // 수량 수정 요청
        mockMvc.perform(put("/cart/" + cartId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        // 다시 조회해서 수량 확인
        MvcResult result2 = mockMvc.perform(get("/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String updatedBody = result2.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode updatedJson = objectMapper.readTree(updatedBody);
        int updatedQuantity = updatedJson.get("cartList").get(0).get("quantity").asInt();

        assertThat(updatedQuantity).isEqualTo(5);

        System.out.println("업데이트 전 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(responseBody)
                )
        );

        System.out.println("업데이트 후 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(updatedBody)
                )
        );

    }

    @Test
    @DisplayName("장바구니 항목 삭제 - 성공")
    void deleteCartItem_success() throws Exception {
        TokenAndUserId user = testAuthUtils.registerAndLoginRandomWithUserId();
        String token = user.getToken();

        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("캠핑 의자");
        productRequestDto.setDescription("접이식 방수 캠핑 의자");
        productRequestDto.setPrice(10000L);
        productRequestDto.setStock(2);
        productRequestDto.setThumbnailUrl("https://example.com/image.jpg");

        MvcResult createRes = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        long createdId = objectMapper.readTree(createRes.getResponse().getContentAsString())
                .path("productId").asLong();

        // 장바구니에 항목 추가
        CartRequestDto requestDto = new CartRequestDto();
        requestDto.setProductId(createdId);
        requestDto.setQuantity(3);
        requestDto.setPrice(10000L);

        mockMvc.perform(post("/cart")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        // 장바구니 조회 → cartId 추출
        MvcResult getResult = mockMvc.perform(get("/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String getBody = getResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode json = objectMapper.readTree(getBody);
        Long cartId = json.get("cartList").get(0).get("id").asLong();

        // 삭제 요청
        mockMvc.perform(delete("/cart/" + cartId)
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isNoContent());

        // 다시 조회하여 장바구니가 비어 있는지 확인
        MvcResult deleteResult = mockMvc.perform(get("/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String deleteBody = deleteResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode deleteJson = objectMapper.readTree(deleteBody);

        assertThat(deleteJson.get("cartList").size()).isEqualTo(0); // 빈 배열

        System.out.println("업데이트 후 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(getBody)
                )
        );

        System.out.println("업데이트 후 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(deleteBody)
                )
        );

    }

    @Test
    @DisplayName("장바구니 비어 있을 때 조회")
    void getCart_whenEmpty_returnsEmptyList() throws Exception {
        // 신규 사용자 생성 (장바구니 없음)
        TokenAndUserId user = testAuthUtils.registerAndLoginRandomWithUserId();
        String token = user.getToken();

        // 장바구니 조회 요청
        MvcResult result = mockMvc.perform(get("/cart")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        // 응답 바디 파싱
        String responseBody = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode json = objectMapper.readTree(responseBody);

        // 검증
        assertThat(json.get("cartList")).isEmpty();
        assertThat(json.get("message").asText()).isEqualTo("장바구니 조회 성공");

        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(responseBody)
                )
        );

    }

    @Test
    @DisplayName("장바구니 수량 수정 실패 - 수량 0 이하")
    void updateCartQuantity_fail_invalidQuantity() throws Exception {
        TokenAndUserId user = testAuthUtils.registerAndLoginRandomWithUserId();
        String token = user.getToken();

        ProductRequestDto productRequestDto = new ProductRequestDto();
        productRequestDto.setName("캠핑 의자");
        productRequestDto.setDescription("접이식 방수 캠핑 의자");
        productRequestDto.setPrice(10000L);
        productRequestDto.setStock(2);
        productRequestDto.setThumbnailUrl("https://example.com/image.jpg");

        MvcResult createRes = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header("Authorization", "Bearer " + token)
                        .content(objectMapper.writeValueAsString(productRequestDto)))
                .andExpect(status().isCreated())
                .andReturn();

        long createdId = objectMapper.readTree(createRes.getResponse().getContentAsString())
                .path("productId").asLong();

        // 장바구니 담기
        CartRequestDto requestDto = new CartRequestDto();
        requestDto.setProductId(createdId);
        requestDto.setQuantity(1);
        requestDto.setPrice(10000L);

        mockMvc.perform(post("/cart")
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated());

        // 장바구니 조회
        MvcResult result = mockMvc.perform(get("/cart")
                        .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn();

        String body = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode json = objectMapper.readTree(body);
        Long cartId = json.get("cartList").get(0).get("id").asLong();

        // 수량 0으로 수정 요청
        CartQuantityUpdateRequest invalidDto = new CartQuantityUpdateRequest();
        invalidDto.setQuantity(0);

        mockMvc.perform(put("/cart/" + cartId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andExpect(status().isBadRequest());

        // 수량 -1로 수정 요청
        invalidDto.setQuantity(-1);

        MvcResult mvcResult = mockMvc.perform(put("/cart/" + cartId)
                        .header("Authorization", "Bearer " + token)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidDto)))
                .andReturn();

        String responseBody = mvcResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(responseBody)
                )
        );
    }

}