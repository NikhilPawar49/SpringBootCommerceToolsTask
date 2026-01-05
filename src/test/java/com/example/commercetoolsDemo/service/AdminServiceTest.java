package com.example.commercetoolsDemo.service;

import com.example.commercetoolsDemo.dto.request.CreateCartRequest;
import com.example.commercetoolsDemo.dto.response.CartResponse;
import com.example.commercetoolsDemo.feign.AdminFeignClient;
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
class AdminServiceTest {

    @Mock
    private AdminFeignClient adminFeignClient;

    @Mock
    private ResponseMapper responseMapper;

    @InjectMocks
    private AdminService adminService;

    @Test
    void getCart_shouldReturnCartResponse() {

        Map<String, Object> ctResponse = Map.of(
                "id", "cart-1",
                "version", 1
        );

        CartResponse mapped = CartResponse.builder()
                .id("cart-1")
                .version(1L)
                .build();

        when(adminFeignClient.getCart(any(), any()))
                .thenReturn(ctResponse);

        when(responseMapper.mapToCartResponse(ctResponse))
                .thenReturn(mapped);

        CartResponse response = adminService.getCart("cart-1");

        assertEquals("cart-1", response.getId());
    }

    @Test
    void createCart_shouldReturnCartResponse() {

        CreateCartRequest request = new CreateCartRequest("USD");

        Map<String, Object> ctResponse = Map.of(
                "id", "cart-2",
                "version", 1
        );

        CartResponse mapped = CartResponse.builder()
                .id("cart-2")
                .version(1L)
                .build();

        when(adminFeignClient.createCart(any(), any()))
                .thenReturn(ctResponse);

        when(responseMapper.mapToCartResponse(ctResponse))
                .thenReturn(mapped);

        CartResponse response = adminService.createCart(request);

        assertEquals("cart-2", response.getId());
    }
}
