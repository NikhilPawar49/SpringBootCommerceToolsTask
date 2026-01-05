package com.example.commercetoolsDemo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;
import java.util.List;

// ============= ORDER RESPONSE =============
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrderResponse {
    private String id;
    private Long version;
    private String orderNumber;
    private String orderState;
    private String customerId;

    private List<LineItemResponse> lineItems;
    private MoneyResponse totalPrice;

    private AddressResponse shippingAddress;
    private AddressResponse billingAddress;
    private ShippingInfoResponse shippingInfo;

    private ZonedDateTime createdAt;
    private ZonedDateTime lastModifiedAt;
}
