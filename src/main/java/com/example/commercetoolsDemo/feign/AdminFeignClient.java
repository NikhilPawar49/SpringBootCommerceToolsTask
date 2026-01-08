package com.example.commercetoolsDemo.feign;

import com.example.api.model.CartResponse;
import com.example.api.model.CreateCartRequest;
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
    CartResponse getCart(
            @PathVariable String projectKey,
            @PathVariable String id
    );

    @PostMapping(
            value = "/{projectKey}/carts",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    CartResponse createCart(
            @PathVariable String projectKey,
            @RequestBody CreateCartRequest body
    );

    @DeleteMapping("/{projectKey}/carts/{id}")
    CartResponse deleteCart(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestParam("version") Long version
    );
}
