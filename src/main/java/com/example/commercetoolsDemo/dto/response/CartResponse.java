package com.example.commercetoolsDemo.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

// ============= CART RESPONSE =============
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CartResponse {
    private String id;
    private Long version;
    private String cartState;
    private String customerId;
    private String anonymousId;

    private List<LineItemResponse> lineItems;
    private MoneyResponse totalPrice;
    private MoneyResponse taxedPrice;

    private AddressResponse shippingAddress;
    private AddressResponse billingAddress;
    private ShippingInfoResponse shippingInfo;

    private ZonedDateTime createdAt;
    private ZonedDateTime lastModifiedAt;
}

