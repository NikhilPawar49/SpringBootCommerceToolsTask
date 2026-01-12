package com.example.commercetoolsDemo.feign;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartDraft;
import com.example.commercetoolsDemo.config.AdminFeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "adminClient",
        url = "${ct.apiUrl}",
        configuration = AdminFeignConfig.class
)
public interface AdminFeignClient {

    @GetMapping("/{projectKey}/carts/{id}")
    Cart getCart(
            @PathVariable String projectKey,
            @PathVariable String id
    );

    @PostMapping(
            value = "/{projectKey}/carts",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    Cart createCart(
            @PathVariable String projectKey,
            @RequestBody CartDraft body
    );

    @DeleteMapping("/{projectKey}/carts/{id}")
    Cart deleteCart(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestParam("version") Long version
    );
}
