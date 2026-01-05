package com.example.commercetoolsDemo.feign;

import com.example.commercetoolsDemo.config.CtAuthFeignConfig;
import com.example.commercetoolsDemo.config.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
@FeignClient(
        name = "ctAuthClient",
        url = "${ct.authUrl}",
        configuration = CtAuthFeignConfig.class
)
public interface CtAuthFeignClient {

    @PostMapping(
            value = "/oauth/token",
            consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE
    )
    Map<String, Object> getToken(
            @RequestHeader("Authorization") String basicAuth,
            @RequestParam("grant_type") String grantType,
            @RequestParam("scope") String scope
    );
}
