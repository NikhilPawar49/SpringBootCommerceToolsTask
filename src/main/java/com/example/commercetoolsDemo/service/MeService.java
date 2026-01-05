package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.dto.request.*;
import com.example.commercetoolsDemo.dto.request.CartUpdateRequest.CartAction;
import com.example.commercetoolsDemo.dto.request.CartUpdateRequest.Address;
import com.example.commercetoolsDemo.dto.request.CartUpdateRequest.ShippingMethodReference;
import com.example.commercetoolsDemo.feign.MeFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeService {

    private final MeFeignClient meFeignClient;

    @Value("${ct.projectKey}")
    private String projectKey;

    // Get active cart
    public Object getMyActiveCart(String token) {
        log.info("Fetching active cart");
        return meFeignClient.getMyActiveCart(projectKey, token);
    }

    // Get all carts
    public Object getMyCarts(String token) {
        log.info("Fetching all carts");
        return meFeignClient.getMyCarts(projectKey, token);
    }

    // Get cart by ID
    public Object getMyCartById(String id, String token) {
        log.info("Fetching cart with ID: {}", id);
        return meFeignClient.getMyCartById(projectKey, id, token);
    }

    // Create cart
    public Object createMyCart(String auth, CreateCartRequest body) {
        log.info("Creating cart with request: {}", body);
        return meFeignClient.createMyCart(projectKey, auth, body);
    }

    // Add line item to cart
    public Object addLineItemToCart(String cartId, String token, Long version,
                                    String productId, Integer variantId, Integer quantity) {
        log.info("Adding line item to cart {}: productId={}, variantId={}, quantity={}",
                cartId, productId, variantId, quantity);

        CartAction action = CartAction.builder()
                .action("addLineItem")
                .productId(productId)
                .variantId(variantId)
                .quantity(quantity)
                .build();

        CartUpdateRequest request = CartUpdateRequest.builder()
                .version(version)
                .actions(Collections.singletonList(action))
                .build();

        return meFeignClient.updateMyCart(projectKey, cartId, token, request);
    }

    // Set shipping address
    public Object setShippingAddress(String cartId, String token, Long version,
                                     String streetName, String streetNumber,
                                     String city, String state,
                                     String postalCode, String country) {
        log.info("Setting shipping address for cart {}", cartId);

        Address address = Address.builder()
                .streetName(streetName)
                .streetNumber(streetNumber)
                .city(city)
                .state(state)
                .postalCode(postalCode)
                .country(country)
                .build();

        CartAction action = CartAction.builder()
                .action("setShippingAddress")
                .address(address)
                .build();

        CartUpdateRequest request = CartUpdateRequest.builder()
                .version(version)
                .actions(Collections.singletonList(action))
                .build();

        return meFeignClient.updateMyCart(projectKey, cartId, token, request);
    }

    // Set billing address
    public Object setBillingAddress(String cartId, String token, Long version,
                                    String streetName, String streetNumber,
                                    String city, String state,
                                    String postalCode, String country) {
        log.info("Setting billing address for cart {}", cartId);

        Address address = Address.builder()
                .streetName(streetName)
                .streetNumber(streetNumber)
                .city(city)
                .state(state)
                .postalCode(postalCode)
                .country(country)
                .build();

        CartAction action = CartAction.builder()
                .action("setBillingAddress")
                .address(address)
                .build();

        CartUpdateRequest request = CartUpdateRequest.builder()
                .version(version)
                .actions(Collections.singletonList(action))
                .build();

        return meFeignClient.updateMyCart(projectKey, cartId, token, request);
    }

    // Set shipping method
    public Object setShippingMethod(String cartId, String token, Long version,
                                    String shippingMethodId) {
        log.info("Setting shipping method for cart {}: {}", cartId, shippingMethodId);

        ShippingMethodReference shippingMethod = ShippingMethodReference.builder()
                .typeId("shipping-method")
                .id(shippingMethodId)
                .build();

        CartAction action = CartAction.builder()
                .action("setShippingMethod")
                .shippingMethod(shippingMethod)
                .build();

        CartUpdateRequest request = CartUpdateRequest.builder()
                .version(version)
                .actions(Collections.singletonList(action))
                .build();

        return meFeignClient.updateMyCart(projectKey, cartId, token, request);
    }

    // Update cart (generic method)
    public Object updateMyCart(String cartId, String token, CartUpdateRequest request) {
        log.info("Updating cart {} with actions: {}", cartId, request.getActions());
        return meFeignClient.updateMyCart(projectKey, cartId, token, request);
    }

    // Delete cart
    public Object deleteMyCart(String id, Long version, String token) {
        log.info("Deleting cart with ID: {}", id);
        return meFeignClient.deleteMyCart(projectKey, id, version, token);
    }

    // Create order from cart
    public Object createMyOrder(String token, String cartId, Long cartVersion) {
        log.info("Creating order from cart ID: {}, version: {}", cartId, cartVersion);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .id(cartId)
                .version(cartVersion)
                .build();

        return meFeignClient.createMyOrder(projectKey, token, request);
    }

    // Get my orders
    public Object getMyOrders(String token) {
        log.info("Fetching all orders");
        return meFeignClient.getMyOrders(projectKey, token);
    }

    // Get order by ID
    public Object getMyOrderById(String id, String token) {
        log.info("Fetching order with ID: {}", id);
        return meFeignClient.getMyOrderById(projectKey, id, token);
    }
}