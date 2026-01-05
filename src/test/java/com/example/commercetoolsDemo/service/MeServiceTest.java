package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.dto.request.CreateCartRequest;
import com.example.commercetoolsDemo.dto.response.CartResponse;
import com.example.commercetoolsDemo.feign.MeFeignClient;
import com.example.commercetoolsDemo.mapper.ResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MeServiceTest {

    @Mock
    private MeFeignClient meFeignClient;

    @Mock
    private ResponseMapper responseMapper;

    @InjectMocks
    private MeService meService;

    @Test
    void createMyCart_shouldReturnCartResponse() {

        // Arrange
        CreateCartRequest request = new CreateCartRequest("USD");

        Map<String, Object> ctResponse = Map.of(
                "id", "cart-123",
                "version", 1
        );

        CartResponse mapped = CartResponse.builder()
                .id("cart-123")
                .version(1L)
                .build();

        when(meFeignClient.createMyCart(any(), any(), any()))
                .thenReturn(ctResponse);

        when(responseMapper.mapToCartResponse(ctResponse))
                .thenReturn(mapped);

        // Act
        CartResponse response =
                meService.createMyCart("Bearer token", request);

        // Assert
        assertEquals("cart-123", response.getId());
        assertEquals(1L, response.getVersion());
    }
}

