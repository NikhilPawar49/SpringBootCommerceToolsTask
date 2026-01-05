package com.example.commercetoolsDemo.feign;

import com.example.commercetoolsDemo.config.AdminFeignConfig;
import com.example.commercetoolsDemo.dto.request.CreateCartRequest;
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
    Object getCart(
            @PathVariable String projectKey,
            @PathVariable String id
    );

    @PostMapping(
            value = "/{projectKey}/carts",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    Object createCart(
            @PathVariable String projectKey,
            @RequestBody CreateCartRequest body
    );

    @DeleteMapping("/{projectKey}/carts/{id}")
    Object deleteCart(
            @PathVariable String projectKey,
            @PathVariable String id,
            @RequestParam("version") Long version
    );
}