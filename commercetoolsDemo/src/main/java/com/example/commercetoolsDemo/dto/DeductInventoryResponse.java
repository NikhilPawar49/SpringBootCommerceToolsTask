package com.example.commercetoolsDemo.dto;

import lombok.Data;

@Data
public class DeductInventoryResponse {
    private Boolean success;
    private String message;
    private Integer remainingQuantity;
    private String skuId;
}
