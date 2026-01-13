package com.example.commercetoolsDemo.feign;

import com.commercetools.api.models.product.Product;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "product-client",
        url = "${ct.apiUrl}"
)
public interface ProductFeignClient {

    @GetMapping("/{projectKey}/products/{productId}")
    Product getProductById(
            @PathVariable String projectKey,
            @PathVariable String productId,
            @RequestHeader("Authorization") String token
    );
}
