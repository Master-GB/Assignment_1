package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Employee;
import com.example.employeemanagement.entity.EmployeeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {

    Optional<Employee> findByEmployeeCode(String employeeCode);

    Optional<Employee> findByNic(String nic);

    boolean existsByEmployeeCode(String employeeCode);

    boolean existsByNic(String nic);

    List<Employee> findByStatus(EmployeeStatus status);

    long countByStatus(EmployeeStatus status);
}