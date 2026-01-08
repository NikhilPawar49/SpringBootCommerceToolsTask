package com.example.commercetoolsDemo.service;


import com.example.api.model.CartResponse;
import com.example.api.model.CreateCartRequest;
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
    void createMyCart_shouldReturnCartResponse() {

        // Arrange
        CreateCartRequest request = new CreateCartRequest();
        request.setCurrency("USD");

        CartResponse feignResponse = new CartResponse();
        feignResponse.setId("cart-123");

        when(meFeignClient.createMyCart(any(), any(), any()))
                .thenReturn(feignResponse);

        // Act
        CartResponse response =
                meService.createMyCart("Bearer token", request);

        // Assert
        assertEquals("cart-123", response.getId());
    }
}
