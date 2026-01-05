package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.dto.request.CreateCartRequest;
import com.example.commercetoolsDemo.dto.response.CartResponse;
import com.example.commercetoolsDemo.feign.AdminFeignClient;
import com.example.commercetoolsDemo.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminFeignClient adminFeignClient;
    private final ResponseMapper responseMapper;

    @Value("${ct.projectKey}")
    private String projectKey;

    public CartResponse getCart(String id) {
        Map<String, Object> ctResponse = (Map<String, Object>) adminFeignClient.getCart(projectKey, id);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    public CartResponse createCart(CreateCartRequest body) {
        Map<String, Object> ctResponse = (Map<String, Object>) adminFeignClient.createCart(projectKey, body);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    public CartResponse deleteCart(String id, Long version) {
        Map<String, Object> ctResponse = (Map<String, Object>) adminFeignClient.deleteCart(projectKey, id, version);
        return responseMapper.mapToCartResponse(ctResponse);
    }
}