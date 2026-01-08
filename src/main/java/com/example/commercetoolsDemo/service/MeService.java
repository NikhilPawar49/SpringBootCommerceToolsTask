package com.example.commercetoolsDemo.service;

import com.example.api.model.*;
//import com.example.commercetoolsDemo.dto.request.CartUpdateRequest;
//import com.example.commercetoolsDemo.dto.request.CreateCartRequest;
//import com.example.commercetoolsDemo.dto.request.CreateOrderRequest;
//import com.example.commercetoolsDemo.dto.request.CartUpdateRequest.CartAction;
//import com.example.commercetoolsDemo.dto.request.CartUpdateRequest.Address;
//import com.example.commercetoolsDemo.dto.request.CartUpdateRequest.ShippingMethodReference;
//import com.example.commercetoolsDemo.dto.response.CartListResponse;
//import com.example.commercetoolsDemo.dto.response.CartResponse;
//import com.example.commercetoolsDemo.dto.response.OrderListResponse;
//import com.example.commercetoolsDemo.dto.response.OrderResponse;
import com.example.commercetoolsDemo.feign.MeFeignClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeService {

    private final MeFeignClient meFeignClient;

    @Value("${ct.projectKey}")
    private String projectKey;

    // ================= CART =================

    public CartResponse getMyActiveCart(String token) {
        log.info("MeService#getMyActiveCart started");
        CartResponse response = meFeignClient.getMyActiveCart(projectKey, token);
        log.info("MeService#getMyActiveCart completed | cartId={}", response.getId());
        return response;
    }

    public CartListResponse getMyCarts(String token) {
        log.info("MeService#getMyCarts started");
        CartListResponse response = meFeignClient.getMyCarts(projectKey, token);
        log.info("MeService#getMyCarts completed | total={}", response.getTotal());
        return response;
    }

    public CartResponse getMyCartById(String id, String token) {
        log.info("MeService#getMyCartById started | cartId={}", id);
        CartResponse response = meFeignClient.getMyCartById(projectKey, id, token);
        log.info("MeService#getMyCartById completed | cartId={}", id);
        return response;
    }

    public CartResponse createMyCart(String token, CreateCartRequest request) {
        log.info("MeService#createMyCart started");
        log.debug("CreateCartRequest={}", request);
        CartResponse response = meFeignClient.createMyCart(projectKey, token, request);
        log.info("MeService#createMyCart completed | cartId={}", response.getId());
        return response;
    }

    // ================= CART ACTIONS =================

    public CartResponse addLineItemToCart(
            String cartId,
            String token,
            Long version,
            String productId,
            Integer variantId,
            Integer quantity
    ) {
        log.info("MeService#addLineItemToCart started | cartId={}", cartId);

        CartAction action = new CartAction();
        action.setAction("addLineItem");
        action.setProductId(productId);
        action.setVariantId(variantId);
        action.setQuantity(quantity);

        CartUpdateRequest request = new CartUpdateRequest();
        request.setVersion(version);
        request.setActions(List.of(action));

        CartResponse response =
                meFeignClient.updateMyCart(projectKey, cartId, token, request);

        log.info("MeService#addLineItemToCart completed | cartId={}", cartId);
        return response;
    }

    public CartResponse setShippingAddress(
            String cartId,
            String token,
            Long version,
            String streetName,
            String streetNumber,
            String city,
            String state,
            String postalCode,
            String country
    ) {
        log.info("MeService#setShippingAddress started | cartId={}", cartId);

        Address address = new Address();
        address.setStreetName(streetName);
        address.setStreetNumber(streetNumber);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(postalCode);
        address.setCountry(country);

        CartAction action = new CartAction();
        action.setAction("setShippingAddress");
        action.setAddress(address);

        CartUpdateRequest request = new CartUpdateRequest();
        request.setVersion(version);
        request.setActions(List.of(action));

        CartResponse response =
                meFeignClient.updateMyCart(projectKey, cartId, token, request);

        log.info("MeService#setShippingAddress completed | cartId={}", cartId);
        return response;
    }

    public CartResponse setBillingAddress(
            String cartId,
            String token,
            Long version,
            String streetName,
            String streetNumber,
            String city,
            String state,
            String postalCode,
            String country
    ) {
        log.info("MeService#setBillingAddress started | cartId={}", cartId);

        Address address = new Address();
        address.setStreetName(streetName);
        address.setStreetNumber(streetNumber);
        address.setCity(city);
        address.setState(state);
        address.setPostalCode(postalCode);
        address.setCountry(country);

        CartAction action = new CartAction();
        action.setAction("setBillingAddress");
        action.setAddress(address);

        CartUpdateRequest request = new CartUpdateRequest();
        request.setVersion(version);
        request.setActions(List.of(action));

        CartResponse response =
                meFeignClient.updateMyCart(projectKey, cartId, token, request);

        log.info("MeService#setBillingAddress completed | cartId={}", cartId);
        return response;
    }

    public CartResponse setShippingMethod(
            String cartId,
            String token,
            Long version,
            String shippingMethodId
    ) {
        log.info("MeService#setShippingMethod started | cartId={}", cartId);

        ShippingMethodReference shippingMethod = new ShippingMethodReference();
        shippingMethod.setTypeId("shipping-method");
        shippingMethod.setId(shippingMethodId);

        CartAction action = new CartAction();
        action.setAction("setShippingMethod");
        action.setShippingMethod(shippingMethod);

        CartUpdateRequest request = new CartUpdateRequest();
        request.setVersion(version);
        request.setActions(List.of(action));

        CartResponse response =
                meFeignClient.updateMyCart(projectKey, cartId, token, request);

        log.info("MeService#setShippingMethod completed | cartId={}", cartId);
        return response;
    }

    public CartResponse updateMyCart(String cartId, String token, CartUpdateRequest request) {
        log.info("MeService#updateMyCart started | cartId={}", cartId);
        CartResponse response =
                meFeignClient.updateMyCart(projectKey, cartId, token, request);
        log.info("MeService#updateMyCart completed | cartId={}", cartId);
        return response;
    }

    public CartResponse deleteMyCart(String id, Long version, String token) {
        log.info("MeService#deleteMyCart started | cartId={}, version={}", id, version);
        CartResponse response =
                meFeignClient.deleteMyCart(projectKey, id, version, token);
        log.info("MeService#deleteMyCart completed | cartId={}", id);
        return response;
    }

    // ================= ORDER =================

    public OrderResponse createMyOrder(String token, String cartId, Long cartVersion) {
        log.info("MeService#createMyOrder started | cartId={}, version={}", cartId, cartVersion);

        CreateOrderRequest request = new CreateOrderRequest();
        request.setCartId(cartId);
        request.setCartVersion(cartVersion);

        OrderResponse response =
                meFeignClient.createMyOrder(projectKey, token, request);

        log.info("MeService#createMyOrder completed | orderId={}", response.getId());
        return response;
    }


    public OrderListResponse getMyOrders(String token) {
        log.info("MeService#getMyOrders started");
        OrderListResponse response =
                meFeignClient.getMyOrders(projectKey, token);
        log.info("MeService#getMyOrders completed | total={}", response.getTotal());
        return response;
    }

    public OrderResponse getMyOrderById(String id, String token) {
        log.info("MeService#getMyOrderById started | orderId={}", id);
        OrderResponse response =
                meFeignClient.getMyOrderById(projectKey, id, token);
        log.info("MeService#getMyOrderById completed | orderId={}", id);
        return response;
    }
}
