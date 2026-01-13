package com.example.commercetoolsDemo.service;

import com.commercetools.api.models.cart.Cart;
import com.example.commercetoolsDemo.feign.AdminFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminFeignClient adminFeignClient;

    @InjectMocks
    private AdminService adminService;
    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(adminService, "projectKey", "test-project");
    }
    @Test
    void getCart_shouldReturnCart() {

        // Arrange
        Cart feignResponse = org.mockito.Mockito.mock(Cart.class);
        when(feignResponse.getId()).thenReturn("cart-1");

        when(adminFeignClient.getCart(any(), any()))
                .thenReturn(feignResponse);

        // Act
        Cart response = adminService.getCart("cart-1");

        // Assert
        assertEquals("cart-1", response.getId());
    }

    @Test
    void createCart_shouldReturnCart() {

        // Arrange
        Cart feignResponse = org.mockito.Mockito.mock(Cart.class);
        when(feignResponse.getId()).thenReturn("cart-2");

        when(adminFeignClient.createCart(any(), any()))
                .thenReturn(feignResponse);

        // Act
        Cart response = adminService.createCart(null); // CartDraft is handled inside service or controller

        // Assert
        assertEquals("cart-2", response.getId());
    }
}
