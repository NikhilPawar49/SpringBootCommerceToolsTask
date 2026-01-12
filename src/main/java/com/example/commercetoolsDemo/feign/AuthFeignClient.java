package com.example.commercetoolsDemo.feign;

import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerDraft;
import com.commercetools.api.models.customer.CustomerSignInResult;
import com.commercetools.api.models.customer.CustomerSignin;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(
        name = "authClient",
        url = "${ct.apiUrl}"
)
public interface AuthFeignClient {

    @PostMapping("/{projectKey}/customers")
    Customer createCustomer(
            @PathVariable String projectKey,
            @RequestBody CustomerDraft request
    );

    @PostMapping("/{projectKey}/login")
    CustomerSignInResult customerLogin(
            @PathVariable String projectKey,
            @RequestBody CustomerSignin request
    );


    @GetMapping("/{projectKey}/me")
    Customer getCustomerInfo(
            @PathVariable String projectKey,
            @RequestHeader("Authorization") String token
    );
}
