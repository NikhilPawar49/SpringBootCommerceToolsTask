package com.example.commercetoolsDemo.service;

import com.example.api.model.*;
import com.example.commercetoolsDemo.dto.LoginRequest;
import com.example.commercetoolsDemo.feign.AuthFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthFeignClient authFeignClient;

    @Value("${ct.projectKey}")
    private String projectKey;

    // ================= CREATE CUSTOMER =================

    public CustomerResponse createCustomer(CreateCustomerRequest request) {
        log.debug("START createCustomer | email={}", request.getEmail());

        CustomerResponse response =
                authFeignClient.createCustomer(projectKey, request);

        log.debug("END createCustomer | customerId={}", response.getId());
        return response;
    }

    // ================= LOGIN =================

    public LoginResponse loginCustomer(String email, String password) {
        log.debug("START loginCustomer | email={}", email);

        LoginRequest request = new LoginRequest();
        request.setEmail(email);
        request.setPassword(password);

        LoginResponse response =
                authFeignClient.customerLogin(projectKey, request);

        log.debug("END loginCustomer | customerId={}",
                response.getCustomer() != null ? response.getCustomer().getId() : null);

        return response;
    }

    // ================= GET CUSTOMER INFO =================

    public CustomerResponse getCustomerInfo(String token) {
        log.debug("START getCustomerInfo");

        CustomerResponse response =
                authFeignClient.getCustomerInfo(projectKey, token);

        log.debug("END getCustomerInfo | customerId={}", response.getId());
        return response;
    }
}
