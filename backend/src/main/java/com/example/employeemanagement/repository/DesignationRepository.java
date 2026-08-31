package com.example.employeemanagement.repository;

import com.example.employeemanagement.entity.Designation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DesignationRepository extends JpaRepository<Designation, Long> {
}