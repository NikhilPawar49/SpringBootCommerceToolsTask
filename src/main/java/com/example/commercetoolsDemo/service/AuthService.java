package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.dto.request.CreateCustomerRequest;
import com.example.commercetoolsDemo.feign.AuthFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthFeignClient authFeignClient;

    @Value("${ct.projectKey}")
    private String projectKey;

    public Object createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer with email: {}", request.getEmail());
        return authFeignClient.createCustomer(projectKey, request);
    }

    public Object loginCustomer(String email, String password) {
        log.info("Customer login attempt for email: {}", email);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        return authFeignClient.customerLogin(projectKey, credentials);
    }

    public Object getCustomerInfo(String token) {
        log.info("Fetching customer info");
        return authFeignClient.getCustomerInfo(projectKey, token);
    }
}