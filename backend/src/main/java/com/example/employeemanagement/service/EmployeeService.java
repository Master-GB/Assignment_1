package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.EmployeeCreateRequest;
import com.example.employeemanagement.dto.EmployeeResponse;
import com.example.employeemanagement.dto.EmployeeStatusUpdateRequest;
import com.example.employeemanagement.dto.EmployeeUpdateRequest;
import com.example.employeemanagement.entity.Designation;
import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.exception.DuplicateResourceException;
import com.example.employeemanagement.exception.ResourceNotFoundException;
import com.example.employeemanagement.repository.DesignationRepository;
import com.example.employeemanagement.repository.EmployeeRepository;
import com.example.employeemanagement.entity.EmployeeStatus;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final DesignationRepository designationRepository;



    @Transactional
    public EmployeeResponse createEmployee(
            EmployeeCreateRequest request) {

        if (employeeRepository.existsByEmployeeCode(
                request.getEmployeeCode())) {

            throw new DuplicateResourceException(
                    "Employee code already exists: "
                            + request.getEmployeeCode()
            );
        }

        if (employeeRepository.existsByNic(
                request.getNic())) {

            throw new DuplicateResourceException(
                    "NIC already exists: "
                            + request.getNic()
            );
        }

        Designation designation =
                designationRepository.findById(
                        request.getDesignationId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Designation not found with ID: "
                                        + request.getDesignationId()
                        )
                );

        // Create Employee entity
        Employee employee = new Employee();

        employee.setEmployeeCode(
                request.getEmployeeCode()
        );

        employee.setFirstName(
                request.getFirstName()
        );

        employee.setLastName(
                request.getLastName()
        );

        employee.setAddress(
                request.getAddress()
        );

        employee.setNic(
                request.getNic()
        );

        employee.setMobileNo(
                request.getMobileNo()
        );

        employee.setGender(
                request.getGender()
        );

        employee.setEmail(
                request.getEmail()
        );

        employee.setDesignation(
                designation
        );

        employee.setProfileImage(
                request.getProfileImage()
        );

        employee.setDateOfBirth(
                request.getDateOfBirth()
        );

        employee.setStatus(EmployeeStatus.ACTIVE);

        Employee savedEmployee =
                employeeRepository.save(employee);

        return mapToResponse(savedEmployee);
    }


    @Transactional(readOnly = true)
    public List<EmployeeResponse> getAllEmployees() {

        return employeeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }


    @Transactional(readOnly = true)
    public EmployeeResponse getEmployeeById(Long id) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found with ID: "
                                                + id
                                )
                        );

        return mapToResponse(employee);
    }


    @Transactional
    public EmployeeResponse updateEmployee(
            Long id,
            EmployeeUpdateRequest request) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found with ID: "
                                                + id
                                )
                        );



        if (!employee.getEmployeeCode()
                .equals(request.getEmployeeCode())
                && employeeRepository.existsByEmployeeCode(
                        request.getEmployeeCode())) {

            throw new DuplicateResourceException(
                    "Employee code already exists: "
                            + request.getEmployeeCode()
            );
        }


        if (!employee.getNic()
                .equals(request.getNic())
                && employeeRepository.existsByNic(
                        request.getNic())) {

            throw new DuplicateResourceException(
                    "NIC already exists: "
                            + request.getNic()
            );
        }


        Designation designation =
                designationRepository.findById(
                        request.getDesignationId()
                ).orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Designation not found with ID: "
                                        + request.getDesignationId()
                        )
                );


        employee.setEmployeeCode(
                request.getEmployeeCode()
        );

        employee.setFirstName(
                request.getFirstName()
        );

        employee.setLastName(
                request.getLastName()
        );

        employee.setAddress(
                request.getAddress()
        );

        employee.setNic(
                request.getNic()
        );

        employee.setMobileNo(
                request.getMobileNo()
        );

        employee.setGender(
                request.getGender()
        );

        employee.setEmail(
                request.getEmail()
        );

        employee.setDesignation(
                designation
        );

        employee.setProfileImage(
                request.getProfileImage()
        );

        employee.setDateOfBirth(
                request.getDateOfBirth()
        );


        Employee updatedEmployee =
                employeeRepository.save(employee);

        return mapToResponse(updatedEmployee);
    }


    @Transactional
    public EmployeeResponse updateEmployeeStatus(
            Long id,
            EmployeeStatusUpdateRequest request) {

        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found with ID: "
                                                + id
                                )
                        );


        employee.setStatus(
                request.getStatus()
        );

    
        Employee updatedEmployee =
                employeeRepository.save(employee);

        return mapToResponse(updatedEmployee);
    }


    @Transactional
    public void deleteEmployee(Long id) {

        // Find employee
        Employee employee =
                employeeRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Employee not found with ID: "
                                                + id
                                )
                        );


        // Delete employee
        employeeRepository.delete(employee);
    }


    private EmployeeResponse mapToResponse(
            Employee employee) {

        EmployeeResponse response =
                new EmployeeResponse();

        response.setId(
                employee.getId()
        );

        response.setEmployeeCode(
                employee.getEmployeeCode()
        );

        response.setFirstName(
                employee.getFirstName()
        );

        response.setLastName(
                employee.getLastName()
        );

        response.setAddress(
                employee.getAddress()
        );

        response.setNic(
                employee.getNic()
        );

        response.setMobileNo(
                employee.getMobileNo()
        );

        response.setGender(
                employee.getGender()
        );

        response.setEmail(
                employee.getEmail()
        );


        // Designation information
        if (employee.getDesignation() != null) {

            response.setDesignationId(
                    employee.getDesignation().getId()
            );

            response.setDesignationName(
                    employee.getDesignation()
                            .getDesignationTitle()
            );
        }


        response.setProfileImage(
                employee.getProfileImage()
        );

        response.setDateOfBirth(
                employee.getDateOfBirth()
        );

        response.setStatus(
                employee.getStatus()
        );

        return response;
    }
}