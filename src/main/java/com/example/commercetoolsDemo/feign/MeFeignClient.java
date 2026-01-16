package com.example.commercetoolsDemo.feign;

import com.example.commercetoolsDemo.dto.request.CartUpdateRequest;
import com.example.commercetoolsDemo.dto.request.CreateCartRequest;
import com.example.commercetoolsDemo.dto.request.CreateOrderRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "meClient",
        url = "${ct.apiUrl}"
)
public interface MeFeignClient {

    // Get active cart
    @GetMapping("/{projectKey}/me/active-cart")
    Object getMyActiveCart(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );

    // Get all carts
    @GetMapping("/{projectKey}/me/carts")
    Object getMyCarts(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );

    // Get cart by ID
    @GetMapping("/{projectKey}/me/carts/{id}")
    Object getMyCartById(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    );

    // Create cart
    @PostMapping(
            value = "/{projectKey}/me/carts",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    Object createMyCart(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token,
            @RequestBody CreateCartRequest body
    );

    // Update cart (add items, set address, etc.)
    @PostMapping(
            value = "/{projectKey}/me/carts/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    Object updateMyCart(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody CartUpdateRequest body
    );

    // Delete cart
    @DeleteMapping("/{projectKey}/me/carts/{id}")
    Object deleteMyCart(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestParam("version") Long version,
            @RequestHeader("Authorization") String token
    );

    // Create order from cart
    @PostMapping(
            value = "/{projectKey}/me/orders",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    Object createMyOrder(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token,
            @RequestBody CreateOrderRequest body
    );

    // Get my orders
    @GetMapping("/{projectKey}/me/orders")
    Object getMyOrders(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );

    // Get order by ID
    @GetMapping("/{projectKey}/me/orders/{id}")
    Object getMyOrderById(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    );
}