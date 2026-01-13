package com.example.commercetoolsDemo.feign;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartDraft;
import com.commercetools.api.models.cart.CartPagedQueryResponse;
import com.commercetools.api.models.cart.CartUpdate;
import com.commercetools.api.models.order.Order;
import com.commercetools.api.models.order.OrderFromCartDraft;
import com.commercetools.api.models.order.OrderPagedQueryResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(
        name = "meClient",
        url = "${ct.apiUrl}"
)
public interface MeFeignClient {

    @GetMapping("/{projectKey}/me/active-cart")
    Cart getMyActiveCart(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/{projectKey}/me/carts")
    CartPagedQueryResponse getMyCarts(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/{projectKey}/me/carts/{id}")
    Cart getMyCartById(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    );

    @PostMapping("/{projectKey}/me/carts")
    Cart createMyCart(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token,
            @RequestBody CartDraft request
    );

    @PostMapping("/{projectKey}/me/carts/{id}")
    Cart updateMyCart(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestHeader("Authorization") String token,
            @RequestBody CartUpdate request
    );

    @DeleteMapping("/{projectKey}/me/carts/{id}")
    Cart deleteMyCart(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestParam Long version,
            @RequestHeader("Authorization") String token
    );

    @PostMapping("/{projectKey}/me/orders")
    Order createMyOrder(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token,
            @RequestBody OrderFromCartDraft body
    );


    @GetMapping("/{projectKey}/me/orders")
    OrderPagedQueryResponse getMyOrders(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );

    @GetMapping("/{projectKey}/me/orders/{id}")
    Order getMyOrderById(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestHeader("Authorization") String token
    );
}
