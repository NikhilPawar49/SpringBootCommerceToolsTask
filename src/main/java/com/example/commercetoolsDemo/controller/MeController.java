package com.example.commercetoolsDemo.controller;

import com.commercetools.api.models.cart.CartDraft;
import com.commercetools.api.models.cart.CartUpdate;
import com.commercetools.api.models.order.OrderFromCartDraft;
import com.example.api.model.*;
import com.example.commercetoolsDemo.service.MeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final MeService meService;

    // ========= CART =========

    @GetMapping("/cart/active")
    public CartResponse getMyActiveCart(
            @RequestHeader("Authorization") String token
    ) {
        return meService.getMyActiveCart(token);
    }

    @GetMapping("/cart/{id}")
    public CartResponse getMyCartById(
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    ) {
        return meService.getMyCartById(id, token);
    }

    @PostMapping("/cart")
    public CartResponse createMyCart(
            @RequestHeader("Authorization") String token,
            @RequestBody CartDraft draft
    ) {
        return meService.createMyCart(token, draft);
    }

    @PostMapping("/cart/{id}")
    public CartResponse updateMyCart(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody CartUpdate update
    ) {
        return meService.updateMyCart(id, token, update);
    }

    @DeleteMapping("/cart/{id}")
    public CartResponse deleteMyCart(
            @PathVariable String id,
            @RequestParam Long version,
            @RequestHeader("Authorization") String token
    ) {
        return meService.deleteMyCart(id, version, token);
    }

    @GetMapping("/cart")
    public CartListResponse getMyCarts(
            @RequestHeader("Authorization") String token
    ) {
        return meService.getMyCarts(token);
    }

    @PostMapping("/cart/{id}/add-item")
    public CartResponse addLineItemToCart(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody AddLineItemRequest body
    ) {
        return meService.addLineItemToCart(
                id,
                token,
                body.getVersion(),
                body.getProductId(),
                body.getVariantId(),
                body.getQuantity()
        );
    }

    @PostMapping("/cart/{id}/shipping-address")
    public CartResponse setShippingAddress(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody AddressRequest body
    ) {
        Address a = body.getAddress();

        return meService.setShippingAddress(
                id,
                token,
                body.getVersion(),
                a.getStreetName(),
                a.getStreetNumber(),
                a.getCity(),
                a.getState(),
                a.getPostalCode(),
                a.getCountry()
        );
    }

    @PostMapping("/cart/{id}/billing-address")
    public CartResponse setBillingAddress(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody AddressRequest body
    ) {
        Address a = body.getAddress();

        return meService.setBillingAddress(
                id,
                token,
                body.getVersion(),
                a.getStreetName(),
                a.getStreetNumber(),
                a.getCity(),
                a.getState(),
                a.getPostalCode(),
                a.getCountry()
        );
    }

    @PostMapping("/cart/{id}/shipping-method")
    public CartResponse setShippingMethod(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody SetShippingMethodRequest body
    ) {
        return meService.setShippingMethod(
                id,
                token,
                body.getVersion(),
                body.getShippingMethodId()
        );
    }

    // ========= ORDER =========

    @PostMapping("/order")
    public OrderResponse createMyOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody OrderFromCartDraft draft
    ) {
        return meService.createMyOrder(token, draft);
    }

    @GetMapping("/order")
    public OrderListResponse getMyOrders(
            @RequestHeader("Authorization") String token
    ) {
        return meService.getMyOrders(token);
    }

    @GetMapping("/order/{id}")
    public OrderResponse getMyOrderById(
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    ) {
        return meService.getMyOrderById(id, token);
    }
}