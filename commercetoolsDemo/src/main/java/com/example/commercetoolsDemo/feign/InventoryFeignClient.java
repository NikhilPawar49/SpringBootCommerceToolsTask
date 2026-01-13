package com.example.commercetoolsDemo.feign;

import com.example.commercetoolsDemo.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "inventory-service",
        url = "${inventory.service.url:http://localhost:8082}"
)
public interface InventoryFeignClient {

    @GetMapping("/inventory/{skuId}")
    InventoryCheckResponse checkInventory(@PathVariable("skuId") String skuId);

    @PostMapping("/inventory/deduct")
    DeductInventoryResponse deductInventory(@RequestBody DeductInventoryRequest request);

    @PostMapping("/inventory/bulk-check")
    BulkInventoryResponse bulkCheckInventory(@RequestBody BulkInventoryRequest request);
}
