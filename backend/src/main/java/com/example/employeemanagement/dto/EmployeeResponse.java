package com.example.employeemanagement.dto;

import com.example.employeemanagement.entity.EmployeeStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeResponse{

    private Long id;
    private String employeeCode;
    private String firstName;
    private String lastName;
    private String address;
    private String nic;
    private String mobileNo;
    private String gender;
    private String email;
    private Long designationId;
    private String designationName;
    private String profileImage;
    private LocalDate dateOfBirth;
    private EmployeeStatus status;
}