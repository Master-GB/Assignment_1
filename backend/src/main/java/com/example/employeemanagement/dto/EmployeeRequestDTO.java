package com.example.employeemanagement.dto;

import com.example.employeemanagement.entity.EmployeeStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequestDTO {

    @NotBlank(message = "Employee code is required")
    private String employeeCode;

    @NotBlank(message = "First name is required")
    private String firstName;

    private String lastName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "NIC is required")
    private String nic;

    private String mobileNo;

    private String gender;

    @Email(message = "Invalid email address")
    private String email;

    @NotNull(message = "Designation is required")
    private Long designationId;

    private String profileImage;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;

    @NotNull(message = "Status is required")
    private EmployeeStatus status = EmployeeStatus.ACTIVE;
}