package com.gear4camp.integration.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gear4camp.dto.auth.LoginRequestDto;
import com.gear4camp.dto.user.UserRegisterRequestDto;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

//@ActiveProfiles("local")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@Transactional
class AuthIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 성공")
    void registerUser_success() throws Exception {
        UserRegisterRequestDto request = new UserRegisterRequestDto();
        request.setUserId("testuser");
        request.setName("테스터");
        request.setPassword("password123");
        request.setEmail("test@example.com");

        // 응답 바디 전체 확인하고 싶을 때(디버깅 등 유용함)
        MvcResult result = mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.message").value("회원 가입 성공"));
                .andReturn();

        String content = result.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(content)
                )
        );

    }

    @Test
    @DisplayName("중복된 아이디로 회원가입 시도 → 실패")
    void registerUser_duplicate_fail() throws Exception {

        // 1. 회원가입
        UserRegisterRequestDto request = new UserRegisterRequestDto();
        request.setUserId("dupuser");
        request.setName("테스터");
        request.setPassword("password123");
        request.setEmail("dup@example.com");

        MvcResult successResult = mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String successContent = successResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(successContent)
                )
        );

        // 2. 중복 시도
        MvcResult conflictResult = mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andReturn();


        String conflictContent = conflictResult.getResponse().getContentAsString(StandardCharsets.UTF_8);

        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(conflictContent)
                )
        );

    }

    @Test
    @DisplayName("로그인 성공")
    void loginUser_success() throws Exception {
        // 회원가입 먼저
        UserRegisterRequestDto request = new UserRegisterRequestDto();
        request.setUserId("loginuser");
        request.setName("테스터");
        request.setPassword("password123");
        request.setEmail("login@example.com");


        MvcResult registerResult = mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerContent = registerResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(registerContent)
                )
        );

        // 로그인
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUserId("loginuser");
        loginRequest.setPassword("password123");

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.token").exists());
                .andReturn();

        String loginContent = loginResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("전체 응답 JSON =\n" +
                        objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                                objectMapper.readTree(loginContent)
                        )
        );

    }

    @Test
    @DisplayName("로그인 실패 - 잘못된 비밀번호")
    void loginUser_fail_wrongPassword() throws Exception {
        // 회원가입 먼저
        UserRegisterRequestDto request = new UserRegisterRequestDto();
        request.setUserId("failuser");
        request.setName("테스터");
        request.setPassword("password123");
        request.setEmail("fail@example.com");

        MvcResult registerResult = mockMvc.perform(post("/users/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String registerContent = registerResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(registerContent)
                )
        );

        // 잘못된 비밀번호 로그인 시도
        LoginRequestDto loginRequest = new LoginRequestDto();
        loginRequest.setUserId("failuser");
        loginRequest.setPassword("wrongpass");

        MvcResult loginResult = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isUnauthorized())
//                .andExpect(jsonPath("$.errorCode").value("INVALID_PASSWORD"));
                .andReturn();

        String loginContent = loginResult.getResponse().getContentAsString(StandardCharsets.UTF_8);
        System.out.println("전체 응답 JSON =\n" +
                objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(
                        objectMapper.readTree(loginContent)
                )
        );

    }
}