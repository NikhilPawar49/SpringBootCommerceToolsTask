package com.example.commercetoolsDemo.mapper;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.LineItem;
import com.commercetools.api.models.common.Money;
import com.example.api.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Component
public class CartMapper {

    public CartResponse toCartResponse(Cart cart) {
        if (cart == null) return null;

        CartResponse response = new CartResponse();
        response.setId(cart.getId());

        response.setLineItems(
                cart.getLineItems() == null
                        ? List.of()
                        : cart.getLineItems().stream()
                        .map(this::toLineItemResponse)
                        .toList()
        );

        response.setTotalPrice(toMoneyResponse(cart.getTotalPrice()));

        // Optional fields (only if present)
        if (cart.getTaxedPrice() != null) {
            response.setTaxedPrice(toMoneyResponse(cart.getTaxedPrice().getTotalNet()));
        }

        if (cart.getShippingAddress() != null) {
            response.setShippingAddress(toAddressResponse(cart.getShippingAddress()));
        }

        if (cart.getBillingAddress() != null) {
            response.setBillingAddress(toAddressResponse(cart.getBillingAddress()));
        }

        if (cart.getShippingInfo() != null) {
            response.setShippingInfo(toShippingInfoResponse(cart.getShippingInfo()));
        }

        return response;
    }

    private LineItemResponse toLineItemResponse(LineItem item) {
        LineItemResponse response = new LineItemResponse();

        response.setId(item.getId());
        response.setProductId(item.getProductId());

        response.setQuantity(
                item.getQuantity() != null
                        ? item.getQuantity().intValue()
                        : null
        );

        response.setName(
                item.getName() != null
                        ? item.getName().get("en")
                        : null
        );

        if (item.getPrice() != null) {
            response.setPrice(toMoneyResponse(item.getPrice().getValue()));
        }

        if (item.getTotalPrice() != null) {
            response.setTotalPrice(toMoneyResponse(item.getTotalPrice()));
        }

        // ❌ NO slug here (not available in Cart LineItem)
        // ❌ NO variantId (not in OpenAPI)

        return response;
    }


    private MoneyResponse toMoneyResponse(
            com.commercetools.api.models.common.Money money
    ) {
        if (money == null) return null;

        MoneyResponse response = new MoneyResponse();
        response.setCurrencyCode(money.getCurrencyCode());

        int fractionDigits = Currency
                .getInstance(money.getCurrencyCode())
                .getDefaultFractionDigits();

        BigDecimal amount = BigDecimal
                .valueOf(money.getCentAmount())
                .movePointLeft(fractionDigits);

        response.setAmount(amount);
        return response;
    }



    private AddressResponse toAddressResponse(
            com.commercetools.api.models.common.Address address
    ) {
        if (address == null) return null;

        AddressResponse response = new AddressResponse();
        response.setStreetName(address.getStreetName());
        response.setStreetNumber(address.getStreetNumber());
        response.setCity(address.getCity());
        response.setState(address.getState());
        response.setPostalCode(address.getPostalCode());
        response.setCountry(address.getCountry());

        return response;
    }

    private ShippingInfoResponse toShippingInfoResponse(
            com.commercetools.api.models.cart.ShippingInfo shippingInfo
    ) {
        if (shippingInfo == null) return null;

        ShippingInfoResponse response = new ShippingInfoResponse();

        response.setShippingMethodName(shippingInfo.getShippingMethodName());

        if (shippingInfo.getPrice() != null) {
            response.setPrice(toMoneyResponse(shippingInfo.getPrice()));
        }

        return response;
    }



}
