package com.example.inventory_service.controller;

import com.example.api.model.*;
import com.example.inventory_service.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{skuId}")
    public ResponseEntity<InventoryResponse> checkInventory(@PathVariable String skuId) {
        log.info("Checking inventory for SKU: {}", skuId);
        InventoryResponse response = inventoryService.getInventoryStatus(skuId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/deduct")
    public ResponseEntity<DeductInventoryResponse> deductInventory(
            @RequestBody DeductInventoryRequest request) {
        log.info("Deduct inventory request for SKU: {}, Quantity: {}, OrderId: {}",
                request.getSkuId(), request.getQuantity(), request.getOrderId());

        DeductInventoryResponse response = inventoryService.deductInventory(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/add")
    public ResponseEntity<InventoryResponse> addInventory(
            @RequestBody AddInventoryRequest request) {
        log.info("Add inventory request for SKU: {}, Quantity: {}",
                request.getSkuId(), request.getQuantity());

        InventoryResponse response = inventoryService.addInventory(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bulk-check")
    public ResponseEntity<BulkInventoryResponse> bulkCheckInventory(
            @RequestBody BulkInventoryRequest request) {
        log.info("Bulk check inventory request for {} items", request.getItems().size());
        BulkInventoryResponse response = inventoryService.checkBulkInventory(request);
        return ResponseEntity.ok(response);
    }
}