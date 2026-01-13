package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.dto.*;
import com.example.commercetoolsDemo.exception.GlobalExceptionHandler.*;
import com.example.commercetoolsDemo.feign.InventoryFeignClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryFeignClient inventoryClient;

    /**
     * Check inventory for a single SKU
     */
    public InventoryCheckResponse checkInventory(String skuId) {
        try {
            log.info("Checking inventory for SKU: {}", skuId);
            InventoryCheckResponse response = inventoryClient.checkInventory(skuId);
            log.info("Inventory check result - SKU: {}, Available: {}, Quantity: {}",
                    skuId, response.getAvailable(), response.getQuantity());
            return response;
        } catch (FeignException.NotFound e) {
            log.error("SKU not found in inventory: {}", skuId);
            throw new InventoryServiceException("Product not found in inventory: " + skuId);
        } catch (FeignException e) {
            log.error("Error checking inventory for SKU: {}", skuId, e);
            throw new InventoryServiceException("Failed to check inventory", e);
        }
    }

    /**
     * Check inventory for multiple SKUs in bulk
     */
    public BulkInventoryResponse checkBulkInventory(List<String> skuIds, List<Integer> quantities) {
        try {
            List<BulkInventoryRequest.InventoryItem> items = new java.util.ArrayList<>();
            for (int i = 0; i < skuIds.size(); i++) {
                items.add(new BulkInventoryRequest.InventoryItem(
                        skuIds.get(i),
                        quantities.get(i)
                ));
            }

            BulkInventoryRequest request = new BulkInventoryRequest(items);
            log.info("Bulk checking inventory for {} items", items.size());

            BulkInventoryResponse response = inventoryClient.bulkCheckInventory(request);
            log.info("Bulk check complete - All available: {}", response.getAllAvailable());

            return response;
        } catch (FeignException e) {
            log.error("Error bulk checking inventory", e);
            throw new InventoryServiceException("Failed to check bulk inventory", e);
        }
    }

    /**
     * Deduct inventory when order is placed
     */
    public DeductInventoryResponse deductInventory(String skuId, Integer quantity, String orderId) {
        try {
            DeductInventoryRequest request = new DeductInventoryRequest(
                    skuId,
                    quantity,
                    orderId
            );

            log.info("Deducting inventory - SKU: {}, Quantity: {}, OrderId: {}",
                    skuId, quantity, orderId);

            DeductInventoryResponse response = inventoryClient.deductInventory(request);

            if (!response.getSuccess()) {
                log.warn("Failed to deduct inventory: {}", response.getMessage());
                throw new InsufficientInventoryException(response.getMessage());
            }

            log.info("Inventory deducted successfully - Remaining: {}",
                    response.getRemainingQuantity());

            return response;
        } catch (FeignException.BadRequest e) {
            log.error("Insufficient inventory for SKU: {}", skuId);
            throw new InsufficientInventoryException("Insufficient inventory for SKU: " + skuId);
        } catch (FeignException.NotFound e) {
            log.error("SKU not found: {}", skuId);
            throw new InventoryServiceException("Product not found: " + skuId);
        } catch (InsufficientInventoryException e) {
            throw e; // Re-throw if already our exception
        } catch (FeignException e) {
            log.error("Error deducting inventory", e);
            throw new InventoryServiceException("Failed to deduct inventory", e);
        }
    }
}