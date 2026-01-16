package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.dto.request.CreateCartRequest;
import com.example.commercetoolsDemo.feign.AdminFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminFeignClient adminFeignClient;

    @Value("${ct.projectKey}")
    private String projectKey;

    public Object getCart(String id) {
        return adminFeignClient.getCart(projectKey, id);
    }

    public Object createCart(CreateCartRequest body) {
        return adminFeignClient.createCart(projectKey, body);
    }

    public Object deleteCart(String id, Long version) {
        return adminFeignClient.deleteCart(projectKey, id, version);
    }
}