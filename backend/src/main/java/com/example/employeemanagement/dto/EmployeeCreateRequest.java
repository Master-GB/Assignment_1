package com.example.employeemanagement.dto;

import com.example.employeemanagement.entity.EmployeeStatus;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeCreateRequest {

    @NotBlank(message = "Employee code is required")
    private String employeeCode;

    @NotBlank(message = "First name is required")
    @Size(max = 50, message = "First name must not exceed 50 characters")
    private String firstName;

    @Size(max = 50, message = "Last name must not exceed 50 characters")
    private String lastName;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "NIC is required")
    @Size(max = 20, message = "NIC must not exceed 20 characters")
    private String nic;

    @Size(max = 20, message = "Mobile number must not exceed 20 characters")
    private String mobileNo;

    private String gender;

    @Email(message = "Invalid email address")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "Designation is required")
    private Long designationId;

    private String profileImage;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}