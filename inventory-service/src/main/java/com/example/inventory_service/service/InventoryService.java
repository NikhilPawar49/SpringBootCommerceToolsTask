package com.example.inventory_service.service;

import com.example.api.model.*;
import com.example.inventory_service.entity.Inventory;
import com.example.inventory_service.exception.GlobalExceptionHandler.InsufficientInventoryException;
import com.example.inventory_service.exception.GlobalExceptionHandler.SkuNotFoundException;
import com.example.inventory_service.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryService {

    private final InventoryRepository inventoryRepository;

    public boolean hasInventory(String skuId) {
        return inventoryRepository.findById(skuId)
                .map(inv -> inv.getQuantity() != null && inv.getQuantity() > 0)
                .orElse(false);
    }

    public InventoryResponse getInventoryStatus(String skuId) {
        Inventory inventory = inventoryRepository.findById(skuId)
                .orElseThrow(() -> new SkuNotFoundException("Invalid SKU ID: " + skuId));

        // Validate product name exists
        if (inventory.getProductName() == null || inventory.getProductName().trim().isEmpty()) {
            throw new SkuNotFoundException("Invalid SKU ID: " + skuId + " (Product name is missing)");
        }

        InventoryResponse response = new InventoryResponse();
        response.setSkuId(inventory.getSkuId());
        response.setAvailable(inventory.getQuantity() > 0);
        response.setQuantity(inventory.getQuantity());
        response.setProductName(inventory.getProductName());

        return response;
    }

    @Transactional
    public DeductInventoryResponse deductInventory(DeductInventoryRequest request) {
        log.info("Deducting inventory for SKU: {}, Quantity: {}, OrderId: {}",
                request.getSkuId(), request.getQuantity(), request.getOrderId());

        Inventory inventory = inventoryRepository.findByIdWithLock(request.getSkuId())
                .orElseThrow(() -> new SkuNotFoundException("Invalid SKU ID: " + request.getSkuId()));

        // Validate product name exists
        if (inventory.getProductName() == null || inventory.getProductName().trim().isEmpty()) {
            throw new SkuNotFoundException("Invalid SKU ID: " + request.getSkuId() + " (Product name is missing)");
        }

        DeductInventoryResponse response = new DeductInventoryResponse();
        response.setSkuId(request.getSkuId());

        if (inventory.getQuantity() < request.getQuantity()) {
            String errorMsg = String.format("Insufficient inventory for SKU: %s. Available: %d, Requested: %d",
                    request.getSkuId(), inventory.getQuantity(), request.getQuantity());
            log.warn(errorMsg);
            throw new InsufficientInventoryException(errorMsg);
        }

        // Deduct the quantity
        inventory.setQuantity(inventory.getQuantity() - request.getQuantity());
        inventoryRepository.save(inventory);

        response.setSuccess(true);
        response.setMessage("Inventory deducted successfully for order: " + request.getOrderId());
        response.setRemainingQuantity(inventory.getQuantity());

        log.info("Successfully deducted {} units for SKU: {}. Remaining: {}",
                request.getQuantity(), request.getSkuId(), inventory.getQuantity());

        return response;
    }

    @Transactional
    public InventoryResponse addInventory(AddInventoryRequest request) {
        log.info("Adding inventory for SKU: {}, Quantity: {}",
                request.getSkuId(), request.getQuantity());

        Inventory inventory = inventoryRepository.findByIdWithLock(request.getSkuId())
                .orElseThrow(() -> new SkuNotFoundException("Invalid SKU ID: " + request.getSkuId()));

        // Validate product name exists
        if (inventory.getProductName() == null || inventory.getProductName().trim().isEmpty()) {
            throw new SkuNotFoundException("Invalid SKU ID: " + request.getSkuId() + " (Product name is missing)");
        }

        inventory.setQuantity(inventory.getQuantity() + request.getQuantity());
        inventoryRepository.save(inventory);

        log.info("Successfully added {} units for SKU: {}. New quantity: {}",
                request.getQuantity(), request.getSkuId(), inventory.getQuantity());

        InventoryResponse response = new InventoryResponse();
        response.setSkuId(inventory.getSkuId());
        response.setAvailable(inventory.getQuantity() > 0);
        response.setQuantity(inventory.getQuantity());
        response.setProductName(inventory.getProductName());

        return response;
    }

    public BulkInventoryResponse checkBulkInventory(BulkInventoryRequest request) {
        log.info("Bulk checking inventory for {} items", request.getItems().size());

        List<String> skuIds = request.getItems().stream()
                .map(InventoryItem::getSkuId)
                .collect(Collectors.toList());

        List<Inventory> inventories = inventoryRepository.findAllBySkuIdIn(skuIds);

        List<InventoryResponse> responses = request.getItems().stream()
                .map(item -> {
                    Inventory inv = inventories.stream()
                            .filter(i -> i.getSkuId().equals(item.getSkuId()))
                            .findFirst()
                            .orElse(null);

                    InventoryResponse response = new InventoryResponse();
                    response.setSkuId(item.getSkuId());

                    // Check if SKU exists and has valid product name
                    if (inv == null || inv.getProductName() == null || inv.getProductName().trim().isEmpty()) {
                        response.setAvailable(false);
                        response.setQuantity(0);
                        response.setProductName(null);
                    } else if (inv.getQuantity() >= item.getQuantity()) {
                        response.setAvailable(true);
                        response.setQuantity(inv.getQuantity());
                        response.setProductName(inv.getProductName());
                    } else {
                        response.setAvailable(false);
                        response.setQuantity(inv.getQuantity());
                        response.setProductName(inv.getProductName());
                    }

                    return response;
                })
                .collect(Collectors.toList());

        BulkInventoryResponse bulkResponse = new BulkInventoryResponse();
        bulkResponse.setAllAvailable(responses.stream().allMatch(InventoryResponse::getAvailable));
        bulkResponse.setItems(responses);

        return bulkResponse;
    }
}