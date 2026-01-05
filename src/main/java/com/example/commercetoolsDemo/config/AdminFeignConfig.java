package com.example.commercetoolsDemo.config;

import com.example.commercetoolsDemo.service.TokenService;
import feign.RequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


public class AdminFeignConfig {

    @Bean
    public RequestInterceptor adminAuthInterceptor(TokenService tokenService) {
        return requestTemplate -> {
            String token = tokenService.getAdminToken();
            requestTemplate.header("Authorization", "Bearer " + token);
        };
    }
}


