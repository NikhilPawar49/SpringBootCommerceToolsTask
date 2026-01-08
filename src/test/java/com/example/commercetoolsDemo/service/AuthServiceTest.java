package com.example.commercetoolsDemo.service;

import com.example.api.model.CreateCustomerRequest;
import com.example.api.model.CustomerResponse;
import com.example.commercetoolsDemo.feign.AuthFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AuthFeignClient authFeignClient;

    @InjectMocks
    private AuthService authService;

    @Test
    void createCustomer_shouldReturnCustomerResponse() {

        // Arrange
        CreateCustomerRequest request = new CreateCustomerRequest();
        request.setEmail("test@example.com");
        request.setPassword("Password@123");
        request.setFirstName("Test");
        request.setLastName("User");

        CustomerResponse feignResponse = new CustomerResponse();
        feignResponse.setId("cust-123");
        feignResponse.setEmail("test@example.com");
        feignResponse.setFirstName("Test");
        feignResponse.setLastName("User");

        when(authFeignClient.createCustomer(any(), any()))
                .thenReturn(feignResponse);

        // Act
        CustomerResponse response = authService.createCustomer(request);

        // Assert
        assertNotNull(response);
        assertEquals("cust-123", response.getId());
        assertEquals("test@example.com", response.getEmail());
    }
}
