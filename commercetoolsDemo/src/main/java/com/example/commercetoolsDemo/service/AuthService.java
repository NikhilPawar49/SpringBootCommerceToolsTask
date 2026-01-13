package com.example.commercetoolsDemo.service;

import com.commercetools.api.models.customer.*;
import com.example.api.model.CustomerResponse;
import com.example.api.model.LoginResponse;
import com.example.commercetoolsDemo.feign.AuthFeignClient;
import com.example.commercetoolsDemo.mapper.CustomerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthFeignClient authFeignClient;
    private final CustomerMapper customerMapper;

    @Value("${ct.projectKey}")
    private String projectKey;

    // ================= CREATE CUSTOMER =================

    public CustomerResponse createCustomer(CustomerDraft request) {
        log.debug("START createCustomer | email={}", request.getEmail());

        Customer customer = authFeignClient.createCustomer(projectKey, request);

        log.debug("END createCustomer | customerId={}", customer.getId());
        return customerMapper.toCustomerResponse(customer);
    }

    // ================= LOGIN =================

    public LoginResponse loginCustomer(String email, String password) {
        log.debug("START loginCustomer | email={}", email);

        CustomerSignin signin = CustomerSignin.builder()
                .email(email)
                .password(password)
                .build();

        CustomerSignInResult result = authFeignClient.customerLogin(projectKey, signin);

        log.debug(
                "END loginCustomer | customerId={}",
                result.getCustomer() != null ? result.getCustomer().getId() : null
        );

        return customerMapper.toLoginResponse(result);
    }

    // ================= GET CUSTOMER INFO =================

    public CustomerResponse getCustomerInfo(String token) {
        log.debug("START getCustomerInfo");

        Customer customer = authFeignClient.getCustomerInfo(projectKey, token);

        log.debug("END getCustomerInfo | customerId={}", customer.getId());
        return customerMapper.toCustomerResponse(customer);
    }
}