package com.example.employeemanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeUpdateRequest {

    @NotBlank(message = "Employee code is required")
    @Size(max = 50, message = "Employee code must not exceed 50 characters")
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
    private String email;

    @NotNull(message = "Designation is required")
    private Long designationId;

    @Past(message = "Date of birth must be in the past")
    private LocalDate dateOfBirth;
}