package com.example.commercetoolsDemo.service;

import com.example.api.model.*;
//import com.example.commercetoolsDemo.dto.request.CreateCartRequest;
//import com.example.commercetoolsDemo.dto.response.CartResponse;
import com.example.commercetoolsDemo.feign.AdminFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminFeignClient adminFeignClient;

    @Value("${ct.projectKey}")
    private String projectKey;

    public CartResponse getCart(String id) {
        log.info("AdminService#getCart started | cartId={}", id);

        CartResponse response = adminFeignClient.getCart(projectKey, id);

        log.info("AdminService#getCart completed | cartId={}", id);
        log.debug("AdminService#getCart response={}", response);

        return response;
    }

    public CartResponse createCart(CreateCartRequest body) {
        log.debug("AdminService#createCart request={}", body);

        CartResponse response = adminFeignClient.createCart(projectKey, body);

        log.info("AdminService#createCart completed | cartId={}", response.getId());

        return response;
    }

    public CartResponse deleteCart(String id, Long version) {
        log.info("AdminService#deleteCart started | cartId={}, version={}", id, version);

        CartResponse response = adminFeignClient.deleteCart(projectKey, id, version);

        log.info("AdminService#deleteCart completed | cartId={}", id);
        log.debug("AdminService#deleteCart response={}", response);

        return response;
    }
}
