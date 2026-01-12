package com.example.commercetoolsDemo.controller;

import com.commercetools.api.models.cart.Cart;
import com.example.commercetoolsDemo.service.MeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeController.class)
@AutoConfigureMockMvc(addFilters = false)
class MeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MeService meService;

    @Test
    void createMyCart_shouldReturnCart() throws Exception {

        Cart cart = org.mockito.Mockito.mock(Cart.class);
        when(cart.getId()).thenReturn("cart-1");

        when(meService.createMyCart(any(), any()))
                .thenReturn(cart);

        mockMvc.perform(post("/me/cart")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "currency": "USD"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart-1"));
    }
}
