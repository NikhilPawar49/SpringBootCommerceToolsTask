package com.example.commercetoolsDemo.feign;

import com.example.api.model.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
@FeignClient(
        name = "meClient",
        url = "${ct.apiUrl}"
)
public interface MeFeignClient {

    @GetMapping("/{projectKey}/me/active-cart")
    CartResponse getMyActiveCart(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/{projectKey}/me/carts")
    CartListResponse getMyCarts(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/{projectKey}/me/carts/{id}")
    CartResponse getMyCartById(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    );

    @PostMapping("/{projectKey}/me/carts")
    CartResponse createMyCart(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token,
            @RequestBody CreateCartRequest request
    );

    @PostMapping("/{projectKey}/me/carts/{id}")
    CartResponse updateMyCart(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody CartUpdateRequest request
    );

    @DeleteMapping("/{projectKey}/me/carts/{id}")
    CartResponse deleteMyCart(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestParam Long version,
            @RequestHeader("Authorization") String token
    );

    @PostMapping("/{projectKey}/me/orders")
    OrderResponse createMyOrder(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token,
            @RequestBody CreateOrderRequest request
    );

    @GetMapping("/{projectKey}/me/orders")
    OrderListResponse getMyOrders(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/{projectKey}/me/orders/{id}")
    OrderResponse getMyOrderById(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    );
}
