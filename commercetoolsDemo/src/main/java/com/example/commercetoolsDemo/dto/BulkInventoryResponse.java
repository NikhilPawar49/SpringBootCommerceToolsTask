package com.example.commercetoolsDemo.dto;

import lombok.Data;

import java.util.List;

@Data
public class BulkInventoryResponse {
    private Boolean allAvailable;
    private List<InventoryCheckResponse> items;
}