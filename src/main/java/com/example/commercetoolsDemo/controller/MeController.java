package com.example.commercetoolsDemo.controller;

import com.example.commercetoolsDemo.dto.request.CartUpdateRequest;
import com.example.commercetoolsDemo.dto.request.CreateCartRequest;
import com.example.commercetoolsDemo.dto.response.*;
import com.example.commercetoolsDemo.service.MeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

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
        return meService.getMyActiveCart(token);
    }

    @GetMapping("/cart")
    public CartListResponse getMyCarts(
            @RequestHeader("Authorization") String token
    ) {
        return meService.getMyCarts(token);
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
            @RequestHeader("Authorization") String auth,
            @RequestBody CreateCartRequest body
    ) {
        return meService.createMyCart(auth, body);
    }

    @PostMapping("/cart/{id}/add-item")
    public CartResponse addLineItemToCart(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> body
    ) {
        Long version = Long.valueOf(body.get("version").toString());
        String productId = (String) body.get("productId");
        Integer variantId = (Integer) body.getOrDefault("variantId", 1);
        Integer quantity = (Integer) body.getOrDefault("quantity", 1);

        return meService.addLineItemToCart(id, token, version, productId, variantId, quantity);
    }

    @PostMapping("/cart/{id}/shipping-address")
    public CartResponse setShippingAddress(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> body
    ) {
        Long version = Long.valueOf(body.get("version").toString());
        Map<String, String> address = (Map<String, String>) body.get("address");

        return meService.setShippingAddress(
                id, token, version,
                address.get("streetName"),
                address.get("streetNumber"),
                address.get("city"),
                address.get("state"),
                address.get("postalCode"),
                address.get("country")
        );
    }

    @PostMapping("/cart/{id}/billing-address")
    public CartResponse setBillingAddress(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> body
    ) {
        Long version = Long.valueOf(body.get("version").toString());
        Map<String, String> address = (Map<String, String>) body.get("address");

        return meService.setBillingAddress(
                id, token, version,
                address.get("streetName"),
                address.get("streetNumber"),
                address.get("city"),
                address.get("state"),
                address.get("postalCode"),
                address.get("country")
        );
    }

    @PostMapping("/cart/{id}/shipping-method")
    public CartResponse setShippingMethod(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> body
    ) {
        Long version = Long.valueOf(body.get("version").toString());
        String shippingMethodId = (String) body.get("shippingMethodId");

        return meService.setShippingMethod(id, token, version, shippingMethodId);
    }

    @PostMapping("/cart/{id}/update")
    public CartResponse updateMyCart(
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody CartUpdateRequest body
    ) {
        return meService.updateMyCart(id, token, body);
    }

    @DeleteMapping("/cart/{id}")
    public CartResponse deleteMyCart(
            @PathVariable String id,
            @RequestParam Long version,
            @RequestHeader("Authorization") String token
    ) {
        return meService.deleteMyCart(id, version, token);
    }

    @PostMapping("/order")
    public OrderResponse createMyOrder(
            @RequestHeader("Authorization") String token,
            @RequestBody Map<String, Object> body
    ) {
        String cartId = (String) body.get("cartId");
        Long cartVersion = Long.valueOf(body.get("cartVersion").toString());

        return meService.createMyOrder(token, cartId, cartVersion);
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