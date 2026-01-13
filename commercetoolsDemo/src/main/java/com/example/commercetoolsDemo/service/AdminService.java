package com.example.commercetoolsDemo.service;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartDraft;
import com.example.api.model.CartResponse;
import com.example.commercetoolsDemo.feign.AdminFeignClient;
import com.example.commercetoolsDemo.mapper.CartMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AdminService {

    private final AdminFeignClient adminFeignClient;
    private final CartMapper cartMapper;

    @Value("${ct.projectKey}")
    private String projectKey;

    public CartResponse getCart(String id) {
        log.info("AdminService#getCart started | cartId={}", id);

        Cart cart = adminFeignClient.getCart(projectKey, id);

        log.info("AdminService#getCart completed | cartId={}, version={}",
                cart.getId(),
                cart.getVersion()
        );
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse createCart(CartDraft draft) {
        log.debug("AdminService#createCart started | currency={}",
                draft.getCurrency()
        );

        Cart cart = adminFeignClient.createCart(projectKey, draft);

        log.info("AdminService#createCart completed | cartId={}, version={}",
                cart.getId(),
                cart.getVersion()
        );
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse deleteCart(String id, Long version) {
        log.info("AdminService#deleteCart started | cartId={}, version={}", id, version);

        Cart cart = adminFeignClient.deleteCart(projectKey, id, version);

        log.info("AdminService#deleteCart completed | cartId={}", id);
        return cartMapper.toCartResponse(cart);
    }
}