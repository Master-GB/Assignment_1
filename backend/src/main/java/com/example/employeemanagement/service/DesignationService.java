package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.DesignationResponseDTO;
import com.example.employeemanagement.entity.Designation;
import com.example.employeemanagement.repository.DesignationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DesignationService {

    private final DesignationRepository designationRepository;

    public List<DesignationResponseDTO> getAllDesignations() {

        return designationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private DesignationResponseDTO mapToResponse(Designation designation) {

        return new DesignationResponseDTO(
                designation.getId(),
                designation.getDesignationTitle()
        );
    }
}