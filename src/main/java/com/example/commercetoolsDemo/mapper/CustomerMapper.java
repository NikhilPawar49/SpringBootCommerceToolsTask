package com.example.commercetoolsDemo.mapper;

import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerSignInResult;
import com.example.api.model.CustomerResponse;
import com.example.api.model.LoginResponse;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {

    // ================= CUSTOMER =================

    public CustomerResponse toCustomerResponse(Customer customer) {
        if (customer == null) return null;

        CustomerResponse response = new CustomerResponse();
        response.setId(customer.getId());
        response.setEmail(customer.getEmail());
        response.setFirstName(customer.getFirstName());
        response.setLastName(customer.getLastName());

        return response;
    }

    // ================= LOGIN =================

    public LoginResponse toLoginResponse(CustomerSignInResult result) {
        if (result == null) return null;

        LoginResponse response = new LoginResponse();

        // commercetools does NOT return tokens
        response.setAccessToken(null);
        response.setRefreshToken(null);
        response.setExpiresIn(null);

        if (result.getCustomer() != null) {
            response.setCustomer(
                    toCustomerResponse(result.getCustomer())
            );
        }

        return response;
    }
}
