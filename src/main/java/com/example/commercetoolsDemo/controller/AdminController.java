package com.example.commercetoolsDemo.controller;

import com.commercetools.api.models.cart.CartDraft;
import com.example.api.model.CartResponse;
import com.example.commercetoolsDemo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/cart/{id}")
    public CartResponse getCart(@PathVariable String id) {
        return adminService.getCart(id);
    }

    @PostMapping("/cart")
    public CartResponse createCart(@RequestBody CartDraft draft) {
        return adminService.createCart(draft);
    }

    @DeleteMapping("/cart/{id}")
    public CartResponse deleteCart(
            @PathVariable String id,
            @RequestParam Long version
    ) {
        return adminService.deleteCart(id, version);
    }
}
