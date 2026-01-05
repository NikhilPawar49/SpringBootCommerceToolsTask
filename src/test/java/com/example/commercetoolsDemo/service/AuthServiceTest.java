package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.dto.request.CreateCustomerRequest;
import com.example.commercetoolsDemo.dto.response.CustomerResponse;
import com.example.commercetoolsDemo.feign.AuthFeignClient;
import com.example.commercetoolsDemo.mapper.ResponseMapper;
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

    @Mock
    private ResponseMapper responseMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void createCustomer_shouldReturnCustomerResponse() {

        // Arrange
        CreateCustomerRequest request = CreateCustomerRequest.builder()
                .email("test@example.com")
                .password("Password@123")
                .firstName("Test")
                .lastName("User")
                .build();

        Map<String, Object> ctResponse = Map.of(
                "customer", Map.of(
                        "id", "cust-123",
                        "email", "test@example.com"
                )
        );

        CustomerResponse mappedResponse = CustomerResponse.builder()
                .id("cust-123")
                .email("test@example.com")
                .build();

        when(authFeignClient.createCustomer(any(), any()))
                .thenReturn(ctResponse);

        when(responseMapper.mapToCustomerResponse(any()))
                .thenReturn(mappedResponse);

        // Act
        CustomerResponse response = authService.createCustomer(request);

        // Assert
        assertNotNull(response);
        assertEquals("cust-123", response.getId());
        assertEquals("test@example.com", response.getEmail());

        verify(authFeignClient).createCustomer(any(), any());
    }
}
