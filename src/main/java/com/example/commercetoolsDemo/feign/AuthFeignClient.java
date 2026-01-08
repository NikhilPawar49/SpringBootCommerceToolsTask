package com.example.commercetoolsDemo.feign;

import com.example.api.model.CreateCustomerRequest;
import com.example.api.model.CustomerResponse;
import com.example.api.model.LoginResponse;
import com.example.commercetoolsDemo.dto.LoginRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "authClient",
        url = "${ct.apiUrl}"
)
public interface AuthFeignClient {

    @PostMapping("/{projectKey}/customers")
    CustomerResponse createCustomer(
            @PathVariable String projectKey,
            @RequestBody CreateCustomerRequest request
    );

    @PostMapping("/{projectKey}/login")
    LoginResponse customerLogin(
            @PathVariable String projectKey,
            @RequestBody LoginRequest request
    );

    @GetMapping("/{projectKey}/me")
    CustomerResponse getCustomerInfo(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );
}
