package com.example.employeemanagement.dto;

import com.example.employeemanagement.entity.EmployeeStatus;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeStatusUpdateRequest {

    @NotNull(message = "Status is required")
    private EmployeeStatus status;
}