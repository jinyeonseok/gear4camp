package com.gear4camp.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.env.Environment;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc // MockMvc 빈을 컨테이너에 생성
public class ProfileCheckTest {
    @Autowired
    Environment env;

    @Test
    void print() {
        System.out.println("ACTIVE=" + String.join(",", env.getActiveProfiles()));
        System.out.println("URL=" + env.getProperty("spring.datasource.url"));
        System.out.println("USER=" + env.getProperty("spring.datasource.username"));
    }
}