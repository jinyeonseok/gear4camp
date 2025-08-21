package com.gear4camp.integration.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gear4camp.dto.auth.LoginRequestDto;
import com.gear4camp.dto.user.UserRegisterRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// 통합 테스트에서 회원가입 + 로그인 후 JWT 토큰을 반환해주는 유틸
@Component
public class TestAuthUtils {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    public String registerAndLogin(String userId, String password) throws Exception {
        String email = userId + "@test.com";

        UserRegisterRequestDto registerDto = new UserRegisterRequestDto();
        registerDto.setUserId(userId);
        registerDto.setName("테스트유저");
        registerDto.setPassword(password);
        registerDto.setEmail(email);

        mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(registerDto)))
                .andExpect(status().isCreated());

        LoginRequestDto loginDto = new LoginRequestDto();
        loginDto.setUserId(userId);
        loginDto.setPassword(password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginDto)))
                .andExpect(status().isOk())
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        JsonNode jsonNode = objectMapper.readTree(content);
        return jsonNode.get("token").asText();
    }

    public String registerAndLoginRandom() throws Exception {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return registerAndLogin("user_" + uuid, "password123");
    }

    public TokenAndUserId registerAndLoginWithUserId(String userId, String password) throws Exception {
        String token = registerAndLogin(userId, password);
        return new TokenAndUserId(token, userId);
    }

    public TokenAndUserId registerAndLoginRandomWithUserId() throws Exception {
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        String userId = "user_" + uuid;
        return registerAndLoginWithUserId(userId, "password123");
    }

    public static class TokenAndUserId {
        private final String token;
        private final String userId;

        public TokenAndUserId(String token, String userId) {
            this.token = token;
            this.userId = userId;
        }

        public String getToken() {
            return token;
        }

        public String getUserId() {
            return userId;
        }
    }
}