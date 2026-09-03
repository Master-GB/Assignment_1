package com.example.employeemanagement.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class TestAuthorizationController {

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminOnly() {
        return "Welcome ADMIN";
    }

    @GetMapping("/hr")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public String hrAccess() {
        return "Welcome ADMIN or HR";
    }

    @GetMapping("/employee")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public String employeeAccess() {
        return "Welcome authenticated employee";
    }
}