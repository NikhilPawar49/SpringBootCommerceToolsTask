package com.example.commercetoolsDemo.feign;

import com.example.commercetoolsDemo.config.AdminFeignConfig;
import com.example.commercetoolsDemo.dto.request.CreateCustomerRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@FeignClient(
        name = "authClient",
        url = "${ct.apiUrl}",
        configuration = AdminFeignConfig.class
)
public interface AuthFeignClient {

    @PostMapping(
            value = "/{projectKey}/customers",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    Object createCustomer(
            @PathVariable String projectKey,
            @RequestBody CreateCustomerRequest body
    );

    @PostMapping(
            value = "/{projectKey}/login",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    Object customerLogin(
            @PathVariable String projectKey,
            @RequestBody Map<String, String> credentials
    );

    @GetMapping("/{projectKey}/me")
    Object getCustomerInfo(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );
}