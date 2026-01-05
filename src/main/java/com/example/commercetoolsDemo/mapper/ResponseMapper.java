package com.example.commercetoolsDemo.mapper;

import com.example.commercetoolsDemo.dto.response.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class ResponseMapper {

    // ============= CART MAPPING =============
    public CartResponse mapToCartResponse(Map<String, Object> ctCart) {
        if (ctCart == null) return null;

        return CartResponse.builder()
                .id((String) ctCart.get("id"))
                .version(getLong(ctCart.get("version")))
                .cartState((String) ctCart.get("cartState"))
                .customerId((String) ctCart.get("customerId"))
                .anonymousId((String) ctCart.get("anonymousId"))
                .lineItems(mapLineItems((List<Map<String, Object>>) ctCart.get("lineItems")))
                .totalPrice(mapMoney((Map<String, Object>) ctCart.get("totalPrice")))
                .taxedPrice(mapMoney(getTaxedPriceMoney(ctCart)))
                .shippingAddress(mapAddress((Map<String, Object>) ctCart.get("shippingAddress")))
                .billingAddress(mapAddress((Map<String, Object>) ctCart.get("billingAddress")))
                .shippingInfo(mapShippingInfo((Map<String, Object>) ctCart.get("shippingInfo")))
                .createdAt(parseDateTime((String) ctCart.get("createdAt")))
                .lastModifiedAt(parseDateTime((String) ctCart.get("lastModifiedAt")))
                .build();
    }

    public CartListResponse mapToCartListResponse(Map<String, Object> ctResponse) {
        if (ctResponse == null) return null;

        List<Map<String, Object>> results = (List<Map<String, Object>>) ctResponse.get("results");

        return CartListResponse.builder()
                .total((Integer) ctResponse.get("total"))
                .results(results == null ? List.of() :
                        results.stream()
                                .map(this::mapToCartResponse)
                                .collect(Collectors.toList()))
                .build();
    }

    // ============= LINE ITEM MAPPING =============
    private List<LineItemResponse> mapLineItems(List<Map<String, Object>> lineItems) {
        if (lineItems == null) return List.of();

        return lineItems.stream()
                .map(this::mapLineItem)
                .collect(Collectors.toList());
    }

    private LineItemResponse mapLineItem(Map<String, Object> item) {
        if (item == null) return null;

        Map<String, Object> name = (Map<String, Object>) item.get("name");
        Map<String, Object> variant = (Map<String, Object>) item.get("variant");

        return LineItemResponse.builder()
                .id((String) item.get("id"))
                .productId((String) item.get("productId"))
                .name(name != null ? (String) name.get("en") : null)
                .quantity((Integer) item.get("quantity"))
                .price(mapMoney((Map<String, Object>) item.get("price")))
                .totalPrice(mapMoney((Map<String, Object>) item.get("totalPrice")))
                .productSlug(getProductSlug(item))
                .imageUrls(getImageUrls(variant))
                .build();
    }

    // ============= MONEY MAPPING =============
    private MoneyResponse mapMoney(Map<String, Object> money) {
        if (money == null) return null;

        return MoneyResponse.builder()
                .currencyCode((String) money.get("currencyCode"))
                .centAmount((Integer) money.get("centAmount"))
                .amount(centToDecimal((Integer) money.get("centAmount")))
                .build();
    }

    // ============= ADDRESS MAPPING =============
    private AddressResponse mapAddress(Map<String, Object> address) {
        if (address == null) return null;

        return AddressResponse.builder()
                .id((String) address.get("id"))
                .streetName((String) address.get("streetName"))
                .streetNumber((String) address.get("streetNumber"))
                .city((String) address.get("city"))
                .state((String) address.get("state"))
                .postalCode((String) address.get("postalCode"))
                .country((String) address.get("country"))
                .build();
    }

    // ============= SHIPPING INFO MAPPING =============
    private ShippingInfoResponse mapShippingInfo(Map<String, Object> shippingInfo) {
        if (shippingInfo == null) return null;

        return ShippingInfoResponse.builder()
                .shippingMethodName((String) shippingInfo.get("shippingMethodName"))
                .price(mapMoney((Map<String, Object>) shippingInfo.get("price")))
                .build();
    }

    // ============= ORDER MAPPING =============
    public OrderResponse mapToOrderResponse(Map<String, Object> ctOrder) {
        if (ctOrder == null) return null;

        return OrderResponse.builder()
                .id((String) ctOrder.get("id"))
                .version(getLong(ctOrder.get("version")))
                .orderNumber((String) ctOrder.get("orderNumber"))
                .orderState((String) ctOrder.get("orderState"))
                .customerId((String) ctOrder.get("customerId"))
                .lineItems(mapLineItems((List<Map<String, Object>>) ctOrder.get("lineItems")))
                .totalPrice(mapMoney((Map<String, Object>) ctOrder.get("totalPrice")))
                .shippingAddress(mapAddress((Map<String, Object>) ctOrder.get("shippingAddress")))
                .billingAddress(mapAddress((Map<String, Object>) ctOrder.get("billingAddress")))
                .shippingInfo(mapShippingInfo((Map<String, Object>) ctOrder.get("shippingInfo")))
                .createdAt(parseDateTime((String) ctOrder.get("createdAt")))
                .lastModifiedAt(parseDateTime((String) ctOrder.get("lastModifiedAt")))
                .build();
    }

    public OrderListResponse mapToOrderListResponse(Map<String, Object> ctResponse) {
        if (ctResponse == null) return null;

        List<Map<String, Object>> results = (List<Map<String, Object>>) ctResponse.get("results");

        return OrderListResponse.builder()
                .total((Integer) ctResponse.get("total"))
                .results(results == null ? List.of() :
                        results.stream()
                                .map(this::mapToOrderResponse)
                                .collect(Collectors.toList()))
                .build();
    }

    // ============= CUSTOMER MAPPING =============
    public CustomerResponse mapToCustomerResponse(Map<String, Object> ctCustomer) {
        if (ctCustomer == null) return null;

        return CustomerResponse.builder()
                .id((String) ctCustomer.get("id"))
                .version(getLong(ctCustomer.get("version")))
                .email((String) ctCustomer.get("email"))
                .firstName((String) ctCustomer.get("firstName"))
                .lastName((String) ctCustomer.get("lastName"))
                .createdAt(parseDateTime((String) ctCustomer.get("createdAt")))
                .build();
    }

    // ============= LOGIN MAPPING =============
    public LoginResponse mapToLoginResponse(Map<String, Object> ctLoginResponse) {
        if (ctLoginResponse == null) return null;

        Map<String, Object> customerData = (Map<String, Object>) ctLoginResponse.get("customer");

        return LoginResponse.builder()
                .accessToken((String) ctLoginResponse.get("access_token"))
                .refreshToken((String) ctLoginResponse.get("refresh_token"))
                .expiresIn((Integer) ctLoginResponse.get("expires_in"))
                .customer(mapToCustomerResponse(customerData))
                .build();
    }

    // ============= HELPER METHODS =============
    private Long getLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        return Long.valueOf(value.toString());
    }

    private BigDecimal centToDecimal(Integer centAmount) {
        if (centAmount == null) return null;
        return BigDecimal.valueOf(centAmount).divide(BigDecimal.valueOf(100));
    }

    private ZonedDateTime parseDateTime(String dateTime) {
        if (dateTime == null) return null;
        return ZonedDateTime.parse(dateTime);
    }

    private String getProductSlug(Map<String, Object> lineItem) {
        Map<String, Object> slug = (Map<String, Object>) lineItem.get("productSlug");
        return slug != null ? (String) slug.get("en") : null;
    }

    private List<String> getImageUrls(Map<String, Object> variant) {
        if (variant == null) return List.of();

        List<Map<String, Object>> images = (List<Map<String, Object>>) variant.get("images");
        if (images == null) return List.of();

        return images.stream()
                .map(img -> (String) img.get("url"))
                .collect(Collectors.toList());
    }

    private Map<String, Object> getTaxedPriceMoney(Map<String, Object> cart) {
        Map<String, Object> taxedPrice = (Map<String, Object>) cart.get("taxedPrice");
        return taxedPrice != null ? (Map<String, Object>) taxedPrice.get("totalNet") : null;
    }
}