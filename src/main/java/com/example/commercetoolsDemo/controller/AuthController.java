package com.example.commercetoolsDemo.controller;

import com.example.commercetoolsDemo.dto.request.CreateCustomerRequest;
import com.example.commercetoolsDemo.dto.response.CustomerResponse;
import com.example.commercetoolsDemo.dto.response.LoginResponse;
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
    public CustomerResponse registerCustomer(@RequestBody CreateCustomerRequest body) {
        return authService.createCustomer(body);
    }

    @PostMapping("/login")
    public LoginResponse loginCustomer(@RequestBody Map<String, String> credentials) {
        String email = credentials.get("email");
        String password = credentials.get("password");
        return authService.loginCustomer(email, password);
    }

    @GetMapping("/me")
    public CustomerResponse getCustomerInfo(@RequestHeader("Authorization") String token) {
        return authService.getCustomerInfo(token);
    }
}