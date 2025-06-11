package com.gear4camp.config;

import java.util.List;

public class ExcludeUrlConfig {
    public static final List<String> EXCLUDE_URLS = List.of(
            "/auth",
            "/users/register",
            "/swagger-ui",
            "/v3/api-docs"
    );
}
