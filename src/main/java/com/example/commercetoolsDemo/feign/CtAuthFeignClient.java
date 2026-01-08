package com.example.commercetoolsDemo.feign;

import com.example.commercetoolsDemo.dto.CtTokenResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
        name = "ctAuthClient",
        url = "${ct.authUrl}"
)
public interface CtAuthFeignClient {

    @PostMapping(
            value = "/oauth/token",
            consumes = "application/x-www-form-urlencoded"
    )
    CtTokenResponse getToken(
            @RequestHeader("Authorization") String authorization,
            @RequestParam("grant_type") String grantType,
            @RequestParam("scope") String scope
    );
}
