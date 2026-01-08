package com.example.commercetoolsDemo.controller;

import com.example.api.model.CartResponse;
import com.example.commercetoolsDemo.service.AdminService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminService adminService;

    @Test
    void getCart_shouldReturnCart() throws Exception {

        CartResponse response = new CartResponse();
        response.setId("cart-1");

        when(adminService.getCart(any()))
                .thenReturn(response);

        mockMvc.perform(get("/admin/cart/cart-1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart-1"));
    }

    @Test
    void createCart_shouldReturnCart() throws Exception {

        CartResponse response = new CartResponse();
        response.setId("cart-2");

        when(adminService.createCart(any()))
                .thenReturn(response);

        mockMvc.perform(post("/admin/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "currency": "USD"
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("cart-2"));
    }
}
