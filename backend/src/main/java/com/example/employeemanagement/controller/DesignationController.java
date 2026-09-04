package com.example.employeemanagement.controller;

import com.example.employeemanagement.dto.DesignationResponseDTO;
import com.example.employeemanagement.service.DesignationService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/designations")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DesignationController {

    private final DesignationService designationService;

    @GetMapping
    public List<DesignationResponseDTO> getAllDesignations() {
        return designationService.getAllDesignations();
    }
}