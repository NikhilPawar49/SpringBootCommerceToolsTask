package com.example.commercetoolsDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class InventoryCheckResponse {
    private String skuId;
    private Boolean available;
    private Integer quantity;
    private String productName;
}
