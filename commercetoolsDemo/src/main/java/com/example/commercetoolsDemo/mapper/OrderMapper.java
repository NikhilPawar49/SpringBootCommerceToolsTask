package com.example.commercetoolsDemo.mapper;

import com.commercetools.api.models.cart.LineItem;
import com.commercetools.api.models.cart.ShippingInfo;
import com.commercetools.api.models.common.Address;
import com.commercetools.api.models.common.Money;
import com.commercetools.api.models.order.Order;
import com.example.api.model.AddressResponse;
import com.example.api.model.LineItemResponse;
import com.example.api.model.MoneyResponse;
import com.example.api.model.OrderResponse;
import com.example.api.model.ShippingInfoResponse;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;

@Component
public class OrderMapper {

    public OrderResponse toOrderResponse(Order order) {
        if (order == null) return null;

        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setOrderNumber(order.getOrderNumber());

        response.setOrderState(
                order.getOrderState() != null
                        ? order.getOrderState().getJsonName()
                        : null
        );

        if (order.getLineItems() != null) {
            response.setLineItems(
                    order.getLineItems().stream()
                            .map(this::toLineItemResponse)
                            .toList()
            );
        }

        response.setTotalPrice(
                order.getTotalPrice() != null
                        ? toMoneyResponse(order.getTotalPrice())
                        : null
        );

        if (order.getShippingAddress() != null) {
            response.setShippingAddress(
                    toAddressResponse(order.getShippingAddress())
            );
        }

        if (order.getShippingInfo() != null) {
            response.setShippingInfo(
                    toShippingInfoResponse(order.getShippingInfo())
            );
        }

        return response;
    }

    // ================= LINE ITEM =================

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
            response.setPrice(
                    toMoneyResponse(item.getPrice().getValue())
            );
        }

        if (item.getTotalPrice() != null) {
            response.setTotalPrice(
                    toMoneyResponse(item.getTotalPrice())
            );
        }

        return response;
    }

    // ================= ADDRESS =================

    private AddressResponse toAddressResponse(Address address) {
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

    // ================= SHIPPING INFO =================

    private ShippingInfoResponse toShippingInfoResponse(ShippingInfo shippingInfo) {
        if (shippingInfo == null) return null;

        ShippingInfoResponse response = new ShippingInfoResponse();
        response.setShippingMethodName(shippingInfo.getShippingMethodName());

        if (shippingInfo.getPrice() != null) {
            response.setPrice(
                    toMoneyResponse(shippingInfo.getPrice())
            );
        }

        return response;
    }

    // ================= MONEY =================

    private MoneyResponse toMoneyResponse(Money money) {
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
}
