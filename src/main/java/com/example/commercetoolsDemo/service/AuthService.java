package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.dto.request.CreateCustomerRequest;
import com.example.commercetoolsDemo.dto.response.CustomerResponse;
import com.example.commercetoolsDemo.dto.response.LoginResponse;
import com.example.commercetoolsDemo.feign.AuthFeignClient;
import com.example.commercetoolsDemo.mapper.ResponseMapper;
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
    private final ResponseMapper responseMapper;

    @Value("${ct.projectKey}")
    private String projectKey;

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.info("Creating customer with email: {}", request.getEmail());
        Map<String, Object> ctResponse = (Map<String, Object>) authFeignClient.createCustomer(projectKey, request);

        // Extract customer from response (commercetools wraps it in "customer" field)
        Map<String, Object> customerData = (Map<String, Object>) ctResponse.get("customer");
        return responseMapper.mapToCustomerResponse(customerData);
    }

    public LoginResponse loginCustomer(String email, String password) {
        log.info("Customer login attempt for email: {}", email);

        Map<String, String> credentials = new HashMap<>();
        credentials.put("email", email);
        credentials.put("password", password);

        Map<String, Object> ctResponse = (Map<String, Object>) authFeignClient.customerLogin(projectKey, credentials);
        return responseMapper.mapToLoginResponse(ctResponse);
    }

    public CustomerResponse getCustomerInfo(String token) {
        log.info("Fetching customer info");
        Map<String, Object> ctResponse = (Map<String, Object>) authFeignClient.getCustomerInfo(projectKey, token);
        return responseMapper.mapToCustomerResponse(ctResponse);
    }
}