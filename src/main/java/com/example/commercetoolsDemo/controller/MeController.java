package com.example.commercetoolsDemo.controller;

import com.example.api.model.*;
import com.example.commercetoolsDemo.service.MeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@Slf4j
@RestController
@RequestMapping("/me")
@RequiredArgsConstructor
public class MeController {

    private final MeService meService;

    // ============= CART OPERATIONS =============

    @GetMapping("/cart/active")
    public CartResponse getMyActiveCart(
            @RequestHeader("Authorization") String token
    ) {
        log.debug("GET /me/cart/active called");
        CartResponse response = meService.getMyActiveCart(token);
        log.debug("GET /me/cart/active completed");
        return response;
    }

    @GetMapping("/cart")
    public CartListResponse getMyCarts(
            @RequestHeader("Authorization") String token
    ) {
        log.debug("GET /me/cart called");
        return meService.getMyCarts(token);
    }

    @GetMapping("/cart/{id}")
    public CartResponse getMyCartById(
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    ) {
        log.debug("GET /me/cart/{} called", id);
        return meService.getMyCartById(id, token);
    }

    @PostMapping("/cart")
    public CartResponse createMyCart(
            @RequestHeader("Authorization") String auth,
            @RequestBody CreateCartRequest body
    ) {
        log.debug("POST /me/cart called with currency={}", body.getCurrency());
        return meService.createMyCart(auth, body);
    }

    @PostMapping("/cart/{id}/add-item")
    public CartResponse addLineItemToCart(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody AddLineItemRequest body
    ) {
        log.debug("POST /me/cart/{}/add-item productId={}", id, body.getProductId());

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
        log.debug("POST /me/cart/{}/shipping-address", id);

        Address address = body.getAddress();

        return meService.setShippingAddress(
                id, token, body.getVersion(),
                address.getStreetName(),
                address.getStreetNumber(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry()
        );
    }

    @PostMapping("/cart/{id}/billing-address")
    public CartResponse setBillingAddress(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody AddressRequest body
    ) {
        log.debug("POST /me/cart/{}/billing-address", id);

        Address address = body.getAddress();

        return meService.setBillingAddress(
                id, token, body.getVersion(),
                address.getStreetName(),
                address.getStreetNumber(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getCountry()
        );
    }

    @PostMapping("/cart/{id}/shipping-method")
    public CartResponse setShippingMethod(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody SetShippingMethodRequest body
    ) {
        log.debug("POST /me/cart/{}/shipping-method {}", id, body.getShippingMethodId());

        return meService.setShippingMethod(
                id, token, body.getVersion(), body.getShippingMethodId()
        );
    }

    @PostMapping("/cart/{id}/update")
    public CartResponse updateMyCart(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody CartUpdateRequest body
    ) {
        log.debug("POST /me/cart/{}/update", id);
        return meService.updateMyCart(id, token, body);
    }

    @DeleteMapping("/cart/{id}")
    public CartResponse deleteMyCart(
            @PathVariable String id,
            @RequestParam Long version,
            @RequestHeader("Authorization") String token
    ) {
        log.debug("DELETE /me/cart/{} version={}", id, version);
        return meService.deleteMyCart(id, version, token);
    }

    // ============= ORDER OPERATIONS =============

    @PostMapping("/order")
    public OrderResponse createMyOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody CreateOrderRequest body
    ) {
        log.debug("POST /me/order cartId={}", body.getCartId());
        return meService.createMyOrder(token, body.getCartId(), body.getCartVersion());
    }

    @GetMapping("/order")
    public OrderListResponse getMyOrders(
            @RequestHeader("Authorization") String token
    ) {
        log.debug("GET /me/order");
        return meService.getMyOrders(token);
    }

    @GetMapping("/order/{id}")
    public OrderResponse getMyOrderById(
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    ) {
        log.debug("GET /me/order/{}", id);
        return meService.getMyOrderById(id, token);
    }
}
