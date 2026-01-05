package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.dto.request.*;
import com.example.commercetoolsDemo.dto.request.CartUpdateRequest.CartAction;
import com.example.commercetoolsDemo.dto.request.CartUpdateRequest.Address;
import com.example.commercetoolsDemo.dto.request.CartUpdateRequest.ShippingMethodReference;
import com.example.commercetoolsDemo.dto.response.*;
import com.example.commercetoolsDemo.feign.MeFeignClient;
import com.example.commercetoolsDemo.mapper.ResponseMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeService {

    private final MeFeignClient meFeignClient;
    private final ResponseMapper responseMapper;

    @Value("${ct.projectKey}")
    private String projectKey;

    // ============= CART OPERATIONS =============

    public CartResponse getMyActiveCart(String token) {
        log.info("Fetching active cart");
        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.getMyActiveCart(projectKey, token);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    public CartListResponse getMyCarts(String token) {
        log.info("Fetching all carts");
        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.getMyCarts(projectKey, token);
        return responseMapper.mapToCartListResponse(ctResponse);
    }

    public CartResponse getMyCartById(String id, String token) {
        log.info("Fetching cart with ID: {}", id);
        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.getMyCartById(projectKey, id, token);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    public CartResponse createMyCart(String auth, CreateCartRequest body) {
        log.info("Creating cart with request: {}", body);
        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.createMyCart(projectKey, auth, body);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    public CartResponse addLineItemToCart(String cartId, String token, Long version,
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

        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.updateMyCart(projectKey, cartId, token, request);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    public CartResponse setShippingAddress(String cartId, String token, Long version,
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

        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.updateMyCart(projectKey, cartId, token, request);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    public CartResponse setBillingAddress(String cartId, String token, Long version,
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

        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.updateMyCart(projectKey, cartId, token, request);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    public CartResponse setShippingMethod(String cartId, String token, Long version,
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

        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.updateMyCart(projectKey, cartId, token, request);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    public CartResponse updateMyCart(String cartId, String token, CartUpdateRequest request) {
        log.info("Updating cart {} with actions: {}", cartId, request.getActions());
        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.updateMyCart(projectKey, cartId, token, request);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    public CartResponse deleteMyCart(String id, Long version, String token) {
        log.info("Deleting cart with ID: {}", id);
        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.deleteMyCart(projectKey, id, version, token);
        return responseMapper.mapToCartResponse(ctResponse);
    }

    // ============= ORDER OPERATIONS =============

    public OrderResponse createMyOrder(String token, String cartId, Long cartVersion) {
        log.info("Creating order from cart ID: {}, version: {}", cartId, cartVersion);

        CreateOrderRequest request = CreateOrderRequest.builder()
                .id(cartId)
                .version(cartVersion)
                .build();

        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.createMyOrder(projectKey, token, request);
        return responseMapper.mapToOrderResponse(ctResponse);
    }

    public OrderListResponse getMyOrders(String token) {
        log.info("Fetching all orders");
        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.getMyOrders(projectKey, token);
        return responseMapper.mapToOrderListResponse(ctResponse);
    }

    public OrderResponse getMyOrderById(String id, String token) {
        log.info("Fetching order with ID: {}", id);
        Map<String, Object> ctResponse = (Map<String, Object>) meFeignClient.getMyOrderById(projectKey, id, token);
        return responseMapper.mapToOrderResponse(ctResponse);
    }
}