package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.DashboardSummaryResponse;
import com.example.employeemanagement.service.DashboardService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(
            DashboardService dashboardService) {

        this.dashboardService = dashboardService;
    }

    @GetMapping("/summary")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<DashboardSummaryResponse> getSummary() {

        return ResponseEntity.ok(
                dashboardService.getSummary()
        );
    }
}