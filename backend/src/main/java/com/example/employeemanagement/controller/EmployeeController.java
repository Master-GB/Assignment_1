package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.EmployeeCreateRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.dto.EmployeeStatusUpdateRequest;
import com.example.employeemanagement.dto.EmployeeUpdateRequest;
import com.example.employeemanagement.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.employeemanagement.entity.EmployeeStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;


    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<EmployeeResponse> createEmployee(
            @Valid @RequestBody EmployeeCreateRequest request) {

        EmployeeResponse response =
                employeeService.createEmployee(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public ResponseEntity<Page<EmployeeResponse>> getAllEmployees(

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            EmployeeStatus status,

            @PageableDefault(
                    size = 10,
                    sort = "firstName",
                    direction = Sort.Direction.ASC
            )
            Pageable pageable) {

        return ResponseEntity.ok(
                employeeService.getAllEmployees(
                        search,
                        status,
                        pageable
                )
        );
    }



    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR', 'EMPLOYEE')")
    public ResponseEntity<EmployeeResponse> getEmployeeById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                employeeService.getEmployeeById(id)
        );
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<EmployeeResponse> updateEmployee(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeUpdateRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployee(id, request)
        );
    }


    @PatchMapping("/{id}/status")
    @PreAuthorize("hasAnyRole('ADMIN', 'HR')")
    public ResponseEntity<EmployeeResponse> updateEmployeeStatus(
            @PathVariable Long id,
            @Valid @RequestBody EmployeeStatusUpdateRequest request) {

        return ResponseEntity.ok(
                employeeService.updateEmployeeStatus(
                        id,
                        request
                )
        );
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long id) {

        employeeService.deleteEmployee(id);

        return ResponseEntity.noContent().build();
    }
}