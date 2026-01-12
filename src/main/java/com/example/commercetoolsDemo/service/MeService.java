package com.example.commercetoolsDemo.service;

import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.common.Address;
import com.commercetools.api.models.order.Order;
import com.commercetools.api.models.order.OrderFromCartDraft;
import com.commercetools.api.models.order.OrderPagedQueryResponse;
import com.commercetools.api.models.shipping_method.ShippingMethodResourceIdentifier;
import com.example.api.model.CartListResponse;
import com.example.api.model.CartResponse;
import com.example.api.model.OrderListResponse;
import com.example.api.model.OrderResponse;
import com.example.commercetoolsDemo.feign.MeFeignClient;
import com.example.commercetoolsDemo.mapper.CartMapper;
import com.example.commercetoolsDemo.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeService {

    private final MeFeignClient meFeignClient;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;

    @Value("${ct.projectKey}")
    private String projectKey;

    // ================= CART =================

    public CartResponse getMyActiveCart(String token) {
        Cart cart = meFeignClient.getMyActiveCart(projectKey, token);
        log.info("MeService#getMyActiveCart completed | cartId={}", cart.getId());
        return cartMapper.toCartResponse(cart);
    }

    public CartListResponse getMyCarts(String token) {
        CartPagedQueryResponse response =
                meFeignClient.getMyCarts(projectKey, token);

        log.info("MeService#getMyCarts completed | total={}", response.getTotal());

        CartListResponse listResponse = new CartListResponse();
        listResponse.setTotal(
                response.getTotal() != null ? response.getTotal().intValue() : 0
        );

        listResponse.setResults(
                response.getResults() == null
                        ? List.of()
                        : response.getResults().stream()
                        .map(cartMapper::toCartResponse)
                        .collect(Collectors.toList())
        );

        return listResponse;
    }

    public CartResponse getMyCartById(String id, String token) {
        log.info("MeService#getMyCartById started | cartId={}", id);
        Cart cart = meFeignClient.getMyCartById(projectKey, id, token);
        log.info("MeService#getMyCartById completed | cartId={}", id);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse createMyCart(String token, CartDraft request) {
        log.info("MeService#createMyCart started");
        Cart cart = meFeignClient.createMyCart(projectKey, token, request);
        log.info("MeService#createMyCart completed | cartId={}", cart.getId());
        return cartMapper.toCartResponse(cart);
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
        log.info("MeService#addLineItemToCart started | cartId={}, version={}", cartId, version);

        CartAddLineItemAction action = CartAddLineItemAction.builder()
                .productId(productId)
                .variantId(variantId.longValue())
                .quantity(quantity.longValue())
                .build();

        CartUpdate update = CartUpdate.builder()
                .version(version)
                .actions(action)
                .build();

        Cart cart = meFeignClient.updateMyCart(projectKey, cartId, token, update);

        log.info("MeService#addLineItemToCart completed | cartId={}, newVersion={}",
                cart.getId(), cart.getVersion());

        return cartMapper.toCartResponse(cart);
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

        Address address = Address.builder()
                .streetName(streetName)
                .streetNumber(streetNumber)
                .city(city)
                .state(state)
                .postalCode(postalCode)
                .country(country)
                .build();

        CartSetShippingAddressAction action =
                CartSetShippingAddressAction.builder()
                        .address(address)
                        .build();

        CartUpdate update = CartUpdate.builder()
                .version(version)
                .actions(action)
                .build();

        Cart cart = meFeignClient.updateMyCart(projectKey, cartId, token, update);

        log.info("MeService#setShippingAddress completed | cartId={}", cartId);
        return cartMapper.toCartResponse(cart);
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

        Address address = Address.builder()
                .streetName(streetName)
                .streetNumber(streetNumber)
                .city(city)
                .state(state)
                .postalCode(postalCode)
                .country(country)
                .build();

        CartSetBillingAddressAction action =
                CartSetBillingAddressAction.builder()
                        .address(address)
                        .build();

        CartUpdate update = CartUpdate.builder()
                .version(version)
                .actions(action)
                .build();

        Cart cart = meFeignClient.updateMyCart(projectKey, cartId, token, update);

        log.info("MeService#setBillingAddress completed | cartId={}", cartId);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse setShippingMethod(
            String cartId,
            String token,
            Long version,
            String shippingMethodId
    ) {
        log.info("MeService#setShippingMethod started | cartId={}", cartId);

        ShippingMethodResourceIdentifier shippingMethod =
                ShippingMethodResourceIdentifier.builder()
                        .id(shippingMethodId)
                        .build();

        CartSetShippingMethodAction action =
                CartSetShippingMethodAction.builder()
                        .shippingMethod(shippingMethod)
                        .build();

        CartUpdate update = CartUpdate.builder()
                .version(version)
                .actions(action)
                .build();

        Cart cart = meFeignClient.updateMyCart(projectKey, cartId, token, update);

        log.info("MeService#setShippingMethod completed | cartId={}", cartId);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse updateMyCart(String cartId, String token, CartUpdate update) {
        log.info("MeService#updateMyCart started | cartId={}, version={}",
                cartId, update.getVersion());

        Cart cart = meFeignClient.updateMyCart(projectKey, cartId, token, update);

        log.info("MeService#updateMyCart completed | cartId={}, newVersion={}",
                cart.getId(), cart.getVersion());

        return cartMapper.toCartResponse(cart);
    }

    public CartResponse deleteMyCart(String cartId, Long version, String token) {
        log.info("MeService#deleteMyCart started | cartId={}, version={}", cartId, version);

        Cart cart = meFeignClient.deleteMyCart(projectKey, cartId, version, token);

        log.info("MeService#deleteMyCart completed | cartId={}", cartId);
        return cartMapper.toCartResponse(cart);
    }

    // ================= ORDER =================

    public OrderResponse createMyOrder(String token, OrderFromCartDraft draft) {
        log.info("Creating order | cartId={}, version={}",
                draft.getCart().getId(),
                draft.getVersion()
        );

        Order order = meFeignClient.createMyOrder(projectKey, token, draft);
        return orderMapper.toOrderResponse(order);
    }

    public OrderListResponse getMyOrders(String token) {
        log.info("MeService#getMyOrders started");

        OrderPagedQueryResponse response =
                meFeignClient.getMyOrders(projectKey, token);

        log.info("MeService#getMyOrders completed | total={}", response.getTotal());

        OrderListResponse listResponse = new OrderListResponse();
        listResponse.setTotal(
                response.getTotal() != null ? response.getTotal().intValue() : 0
        );

        listResponse.setResults(
                response.getResults() == null
                        ? List.of()
                        : response.getResults().stream()
                        .map(orderMapper::toOrderResponse)
                        .collect(Collectors.toList())
        );


        return listResponse;
    }

    public OrderResponse getMyOrderById(String id, String token) {
        log.info("MeService#getMyOrderById started | orderId={}", id);

        Order order = meFeignClient.getMyOrderById(projectKey, id, token);

        log.info("MeService#getMyOrderById completed | orderId={}", id);
        return orderMapper.toOrderResponse(order);
    }
}
