package com.example.commercetoolsDemo.service;

import com.commercetools.api.models.cart.Cart;
import com.commercetools.api.models.cart.CartDraft;
import com.example.commercetoolsDemo.feign.MeFeignClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeServiceTest {

    @Mock
    private MeFeignClient meFeignClient;

    @InjectMocks
    private MeService meService;

    @Test
    void createMyCart_shouldReturnCart() {

        // Arrange
        CartDraft draft = org.mockito.Mockito.mock(CartDraft.class);

        Cart feignResponse = org.mockito.Mockito.mock(Cart.class);
        when(feignResponse.getId()).thenReturn("cart-123");

        when(meFeignClient.createMyCart(any(), any(), any()))
                .thenReturn(feignResponse);

        // Act
        Cart response = meService.createMyCart("Bearer token", draft);

        // Assert
        assertEquals("cart-123", response.getId());
    }
}
