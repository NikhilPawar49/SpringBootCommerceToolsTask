package com.example.commercetoolsDemo.service;

import com.commercetools.api.models.customer.Customer;
import com.commercetools.api.models.customer.CustomerDraft;
import com.commercetools.api.models.customer.CustomerSignInResult;
import com.example.commercetoolsDemo.feign.AuthFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthFeignClient authFeignClient;

    @InjectMocks
    private AuthService authService;

    @Test
    void createCustomer_shouldReturnCustomer() {

        // Arrange
        CustomerDraft draft = org.mockito.Mockito.mock(CustomerDraft.class);
        when(draft.getEmail()).thenReturn("test@example.com");

        Customer customer = org.mockito.Mockito.mock(Customer.class);
        when(customer.getId()).thenReturn("cust-123");

        when(authFeignClient.createCustomer(any(), any()))
                .thenReturn(customer);

        // Act
        Customer response = authService.createCustomer(draft);

        // Assert
        assertNotNull(response);
        assertEquals("cust-123", response.getId());
    }

    @Test
    void loginCustomer_shouldReturnSignInResult() {

        // Arrange
        Customer customer = org.mockito.Mockito.mock(Customer.class);
        when(customer.getId()).thenReturn("cust-123");

        CustomerSignInResult signInResult =
                org.mockito.Mockito.mock(CustomerSignInResult.class);
        when(signInResult.getCustomer()).thenReturn(customer);

        when(authFeignClient.customerLogin(any(), any()))
                .thenReturn(signInResult);

        // Act
        CustomerSignInResult response =
                authService.loginCustomer("test@example.com", "Password@123");

        // Assert
        assertNotNull(response);
        assertEquals("cust-123", response.getCustomer().getId());
    }
}
