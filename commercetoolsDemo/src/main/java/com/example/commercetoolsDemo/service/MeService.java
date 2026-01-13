package com.example.commercetoolsDemo.service;

import com.commercetools.api.models.cart.*;
import com.commercetools.api.models.common.Address;
import com.commercetools.api.models.order.Order;
import com.commercetools.api.models.order.OrderFromCartDraft;
import com.commercetools.api.models.order.OrderPagedQueryResponse;
import com.commercetools.api.models.product.Product;
import com.commercetools.api.models.shipping_method.ShippingMethodResourceIdentifier;
import com.example.api.model.CartListResponse;
import com.example.api.model.CartResponse;
import com.example.api.model.OrderListResponse;
import com.example.api.model.OrderResponse;
import com.example.commercetoolsDemo.dto.*;
import com.example.commercetoolsDemo.exception.GlobalExceptionHandler.*;
import com.example.commercetoolsDemo.feign.MeFeignClient;
import com.example.commercetoolsDemo.feign.ProductFeignClient;
import com.example.commercetoolsDemo.mapper.CartMapper;
import com.example.commercetoolsDemo.mapper.OrderMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class MeService {

    private final MeFeignClient meFeignClient;
    private final CartMapper cartMapper;
    private final OrderMapper orderMapper;
    private final InventoryService inventoryService;
    private final ProductFeignClient productFeignClient;


    @Value("${ct.projectKey}")
    private String projectKey;

    // ================= CART =================

    public CartResponse getMyActiveCart(String token) {
        Cart cart = meFeignClient.getMyActiveCart(projectKey, token);
        log.info("MeService#getMyActiveCart completed | cartId={}", cart.getId());
        return cartMapper.toCartResponse(cart);
    }

    public CartListResponse getMyCarts(String token) {
        CartPagedQueryResponse response =
                meFeignClient.getMyCarts(projectKey, token);

        log.info("MeService#getMyCarts completed | total={}", response.getTotal());

        CartListResponse listResponse = new CartListResponse();
        listResponse.setTotal(
                response.getTotal() != null ? response.getTotal().intValue() : 0
        );

        listResponse.setResults(
                response.getResults() == null
                        ? List.of()
                        : response.getResults().stream()
                        .map(cartMapper::toCartResponse)
                        .collect(Collectors.toList())
        );

        return listResponse;
    }

    public CartResponse getMyCartById(String id, String token) {
        log.info("MeService#getMyCartById started | cartId={}", id);
        Cart cart = meFeignClient.getMyCartById(projectKey, id, token);
        log.info("MeService#getMyCartById completed | cartId={}", id);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse createMyCart(String token, CartDraft request) {
        log.info("MeService#createMyCart started");
        Cart cart = meFeignClient.createMyCart(projectKey, token, request);
        log.info("MeService#createMyCart completed | cartId={}", cart.getId());
        return cartMapper.toCartResponse(cart);
    }

    // ================= CART ACTIONS WITH INVENTORY =================

    /**
     * Add line item to cart WITH inventory check
     */
    public CartResponse addLineItemToCart(
            String cartId,
            String token,
            Long version,
            String productId,
            Integer variantId,
            Integer quantity
    ) {
        log.info("Adding item | productId={}, variantId={}, quantity={}",
                productId, variantId, quantity);

        String skuId = resolveSkuFromProduct(productId, variantId, token);

        log.info("Resolved SKU: {}", skuId);

        InventoryCheckResponse inventoryCheck =
                inventoryService.checkInventory(skuId);

        if (!inventoryCheck.getAvailable()) {
            throw new InsufficientInventoryException(
                    "Product out of stock: " + skuId
            );
        }

        if (inventoryCheck.getQuantity() < quantity) {
            throw new InsufficientInventoryException(
                    "Insufficient stock for SKU " + skuId
            );
        }

        CartAddLineItemAction action = CartAddLineItemAction.builder()
                .productId(productId)
                .variantId(variantId.longValue())
                .quantity(quantity.longValue())
                .build();

        CartUpdate update = CartUpdate.builder()
                .version(version)
                .actions(action)
                .build();

        Cart cart = meFeignClient.updateMyCart(projectKey, cartId, token, update);

        return cartMapper.toCartResponse(cart);
    }


    public CartResponse setShippingAddress(
            String cartId,
            String token,
            Long version,
            String streetName,
            String streetNumber,
            String city,
            String state,
            String postalCode,
            String country
    ) {
        log.info("MeService#setShippingAddress started | cartId={}", cartId);

        Address address = Address.builder()
                .streetName(streetName)
                .streetNumber(streetNumber)
                .city(city)
                .state(state)
                .postalCode(postalCode)
                .country(country)
                .build();

        CartSetShippingAddressAction action =
                CartSetShippingAddressAction.builder()
                        .address(address)
                        .build();

        CartUpdate update = CartUpdate.builder()
                .version(version)
                .actions(action)
                .build();

        Cart cart = meFeignClient.updateMyCart(projectKey, cartId, token, update);

        log.info("MeService#setShippingAddress completed | cartId={}", cartId);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse setBillingAddress(
            String cartId,
            String token,
            Long version,
            String streetName,
            String streetNumber,
            String city,
            String state,
            String postalCode,
            String country
    ) {
        log.info("MeService#setBillingAddress started | cartId={}", cartId);

        Address address = Address.builder()
                .streetName(streetName)
                .streetNumber(streetNumber)
                .city(city)
                .state(state)
                .postalCode(postalCode)
                .country(country)
                .build();

        CartSetBillingAddressAction action =
                CartSetBillingAddressAction.builder()
                        .address(address)
                        .build();

        CartUpdate update = CartUpdate.builder()
                .version(version)
                .actions(action)
                .build();

        Cart cart = meFeignClient.updateMyCart(projectKey, cartId, token, update);

        log.info("MeService#setBillingAddress completed | cartId={}", cartId);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse setShippingMethod(
            String cartId,
            String token,
            Long version,
            String shippingMethodId
    ) {
        log.info("MeService#setShippingMethod started | cartId={}", cartId);

        ShippingMethodResourceIdentifier shippingMethod =
                ShippingMethodResourceIdentifier.builder()
                        .id(shippingMethodId)
                        .build();

        CartSetShippingMethodAction action =
                CartSetShippingMethodAction.builder()
                        .shippingMethod(shippingMethod)
                        .build();

        CartUpdate update = CartUpdate.builder()
                .version(version)
                .actions(action)
                .build();

        Cart cart = meFeignClient.updateMyCart(projectKey, cartId, token, update);

        log.info("MeService#setShippingMethod completed | cartId={}", cartId);
        return cartMapper.toCartResponse(cart);
    }

    public CartResponse updateMyCart(String cartId, String token, CartUpdate update) {
        log.info("MeService#updateMyCart started | cartId={}, version={}",
                cartId, update.getVersion());

        Cart cart = meFeignClient.updateMyCart(projectKey, cartId, token, update);

        log.info("MeService#updateMyCart completed | cartId={}, newVersion={}",
                cart.getId(), cart.getVersion());

        return cartMapper.toCartResponse(cart);
    }

    public CartResponse deleteMyCart(String cartId, Long version, String token) {
        log.info("MeService#deleteMyCart started | cartId={}, version={}", cartId, version);

        Cart cart = meFeignClient.deleteMyCart(projectKey, cartId, version, token);

        log.info("MeService#deleteMyCart completed | cartId={}", cartId);
        return cartMapper.toCartResponse(cart);
    }

    // ================= ORDER WITH INVENTORY DEDUCTION =================

    /**
     * Create order WITH inventory validation and deduction
     */
    public OrderResponse createMyOrder(String token, OrderFromCartDraft draft) {
        log.info("Creating order | cartId={}, version={}",
                draft.getCart().getId(),
                draft.getVersion()
        );

        try {
            // Get the cart to validate inventory for all items
            Cart cart = meFeignClient.getMyCartById(
                    projectKey,
                    draft.getCart().getId(),
                    token
            );

            // Extract SKUs and quantities from line items
            List<String> skuIds = new ArrayList<>();
            List<Integer> quantities = new ArrayList<>();

            for (LineItem item : cart.getLineItems()) {
                String sku = extractSkuFromLineItem(item);
                skuIds.add(sku);
                quantities.add(item.getQuantity().intValue());
            }

            log.info("Validating inventory for {} items before order creation", skuIds.size());

            // BULK CHECK INVENTORY
            BulkInventoryResponse bulkCheck = inventoryService.checkBulkInventory(skuIds, quantities);

            if (!bulkCheck.getAllAvailable()) {
                String unavailableItems = bulkCheck.getItems().stream()
                        .filter(item -> !item.getAvailable())
                        .map(item -> String.format("%s (available: %d)",
                                item.getSkuId(),
                                item.getQuantity()))
                        .collect(Collectors.joining(", "));

                throw new InsufficientInventoryException(
                        "Cannot create order. Insufficient inventory for: " + unavailableItems
                );
            }

            // All items have sufficient inventory, create the order
            Order order = meFeignClient.createMyOrder(projectKey, token, draft);
            log.info("Order created successfully: {}", order.getId());

            // DEDUCT INVENTORY FOR ALL ITEMS
            for (int i = 0; i < skuIds.size(); i++) {
                String skuId = skuIds.get(i);
                Integer quantity = quantities.get(i);

                try {
                    inventoryService.deductInventory(skuId, quantity, order.getId());
                    log.info("Inventory deducted - SKU: {}, Quantity: {}, OrderId: {}",
                            skuId, quantity, order.getId());

                } catch (Exception e) {
                    // Log the error but don't fail the order
                    // In production, you might implement compensation logic
                    log.error("Failed to deduct inventory for SKU: {} in order: {}",
                            skuId, order.getId(), e);
                    // TODO: Implement compensation or manual inventory adjustment
                }
            }

            return orderMapper.toOrderResponse(order);

        } catch (InsufficientInventoryException e) {
            log.error("Cannot create order due to insufficient inventory: {}", e.getMessage());
            throw e;
        } catch (InventoryServiceException e) {
            log.error("Inventory service error during order creation: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("Error creating order", e);
            throw new RuntimeException("Failed to create order: " + e.getMessage(), e);
        }
    }

    public OrderListResponse getMyOrders(String token) {
        log.info("MeService#getMyOrders started");

        OrderPagedQueryResponse response =
                meFeignClient.getMyOrders(projectKey, token);

        log.info("MeService#getMyOrders completed | total={}", response.getTotal());

        OrderListResponse listResponse = new OrderListResponse();
        listResponse.setTotal(
                response.getTotal() != null ? response.getTotal().intValue() : 0
        );

        listResponse.setResults(
                response.getResults() == null
                        ? List.of()
                        : response.getResults().stream()
                        .map(orderMapper::toOrderResponse)
                        .collect(Collectors.toList())
        );

        return listResponse;
    }

    public OrderResponse getMyOrderById(String id, String token) {
        log.info("MeService#getMyOrderById started | orderId={}", id);

        Order order = meFeignClient.getMyOrderById(projectKey, id, token);

        log.info("MeService#getMyOrderById completed | orderId={}", id);
        return orderMapper.toOrderResponse(order);
    }

    // ================= HELPER METHODS =================

    /**
     * Extract SKU from LineItem
     * Adjust this method based on how your SKUs are structured
     */
    private String extractSkuFromLineItem(LineItem item) {
        if (item.getVariant() == null || item.getVariant().getSku() == null) {
            throw new IllegalStateException(
                    "SKU missing for lineItem: " + item.getId()
            );
        }
        return item.getVariant().getSku();
    }

    private String resolveSkuFromProduct(
            String productId,
            Integer variantId,
            String token
    ) {
        Product product = productFeignClient.getProductById(
                projectKey,
                productId,
                token
        );

        return product.getMasterData()
                .getCurrent()
                .getMasterVariant()
                .getId().equals(variantId.longValue())
                ? product.getMasterData().getCurrent().getMasterVariant().getSku()
                : product.getMasterData().getCurrent().getVariants().stream()
                .filter(v -> v.getId().equals(variantId.longValue()))
                .findFirst()
                .map(v -> v.getSku())
                .orElseThrow(() ->
                        new IllegalStateException(
                                "SKU not found for productId=" + productId +
                                        ", variantId=" + variantId
                        )
                );
    }

}