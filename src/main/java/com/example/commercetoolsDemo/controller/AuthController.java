package com.example.commercetoolsDemo.controller;

import com.commercetools.api.models.customer.CustomerDraft;
import com.example.api.model.CustomerResponse;
import com.example.api.model.LoginResponse;
import com.example.commercetoolsDemo.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public CustomerResponse registerCustomer(@RequestBody CustomerDraft body) {
        return authService.createCustomer(body);
    }

    @PostMapping("/login")
    public LoginResponse loginCustomer(@RequestBody Map<String, String> credentials) {
        return authService.loginCustomer(
                credentials.get("email"),
                credentials.get("password")
        );
    }

    @GetMapping("/me")
    public CustomerResponse getCustomerInfo(
            @RequestHeader("Authorization") String token
    ) {
        return authService.getCustomerInfo(token);
    }
}