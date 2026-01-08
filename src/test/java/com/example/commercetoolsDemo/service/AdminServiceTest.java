package com.example.commercetoolsDemo.service;

import com.example.api.model.CartResponse;
import com.example.api.model.CreateCartRequest;
import com.example.commercetoolsDemo.feign.AdminFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminFeignClient adminFeignClient;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getCart_shouldReturnCartResponse() {

        // Arrange
        CartResponse feignResponse = new CartResponse();
        feignResponse.setId("cart-1");

        when(adminFeignClient.getCart(any(), any()))
                .thenReturn(feignResponse);

        // Act
        CartResponse response = adminService.getCart("cart-1");

        // Assert
        assertEquals("cart-1", response.getId());
    }

    @Test
    void createCart_shouldReturnCartResponse() {

        // Arrange
        CreateCartRequest request = new CreateCartRequest();

        CartResponse feignResponse = new CartResponse();
        feignResponse.setId("cart-2");

        when(adminFeignClient.createCart(any(), any()))
                .thenReturn(feignResponse);

        // Act
        CartResponse response = adminService.createCart(request);

        // Assert
        assertEquals("cart-2", response.getId());
    }
}
