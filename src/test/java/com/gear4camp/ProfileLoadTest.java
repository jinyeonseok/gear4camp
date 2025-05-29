package com.gear4camp;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
//@ActiveProfiles("test-local")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
//@TestPropertySource("classpath:application-test-local.properties")
public class ProfileLoadTest {

    @Autowired
    private Environment env;

    @BeforeAll
    void printProfileAndDatasource() {
        System.out.println("🔥 Active Profiles: " + String.join(", ", env.getActiveProfiles()));
        System.out.println("🧪 Datasource URL: " + env.getProperty("spring.datasource.url"));
        System.out.println("🧪 MyBatis Mapper: " + env.getProperty("mybatis.mapper-locations"));
    }

    @Test
    void dummyTest() {
        assertNotNull(env);
    }
}