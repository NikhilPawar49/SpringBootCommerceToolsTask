package com.example.commercetoolsDemo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BulkInventoryRequest {
    private List<InventoryItem> items;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class InventoryItem {
        private String skuId;
        private Integer quantity;
    }
}