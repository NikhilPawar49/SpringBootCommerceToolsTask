package com.example.commercetoolsDemo.controller;

import com.example.commercetoolsDemo.dto.request.CreateCartRequest;
import com.example.commercetoolsDemo.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/cart/{id}")
    public Object getCart(@PathVariable String id) {
        return adminService.getCart(id);
    }

    @PostMapping("/cart")
    public Object createCart(@RequestBody CreateCartRequest body) {
        return adminService.createCart(body);
    }

    @DeleteMapping("/cart/{id}")
    public Object deleteCart(
            @PathVariable String id,
            @RequestParam Long version
    ) {
        return adminService.deleteCart(id, version);
    }
}