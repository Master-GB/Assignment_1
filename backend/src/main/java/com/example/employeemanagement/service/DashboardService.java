package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DashboardSummaryResponse;
import com.example.employeemanagement.entity.EmployeeStatus;
import com.example.employeemanagement.repository.DesignationRepository;
import com.example.employeemanagement.repository.EmployeeRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DashboardService {

    private final EmployeeRepository employeeRepository;
    private final DesignationRepository designationRepository;

    public DashboardService(
            EmployeeRepository employeeRepository,
            DesignationRepository designationRepository) {

        this.employeeRepository = employeeRepository;
        this.designationRepository = designationRepository;
    }

    @Transactional(readOnly = true)
    public DashboardSummaryResponse getSummary() {

        long totalEmployees =
                employeeRepository.count();

        long activeEmployees =
                employeeRepository.countByStatus(
                        EmployeeStatus.ACTIVE
                );

        long inactiveEmployees =
                employeeRepository.countByStatus(
                        EmployeeStatus.INACTIVE
                );

        long totalDesignations =
                designationRepository.count();

        return new DashboardSummaryResponse(
                totalEmployees,
                activeEmployees,
                inactiveEmployees,
                totalDesignations
        );
    }
}