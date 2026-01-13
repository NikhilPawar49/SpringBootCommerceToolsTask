package com.example.commercetoolsDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeductInventoryRequest {
    private String skuId;
    private Integer quantity;
    private String orderId;
}
