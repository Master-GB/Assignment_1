package com.example.employeemanagement.service;

import com.example.employeemanagement.dto.UserRegistrationRequest;
import com.example.employeemanagement.dto.UserResponse;
import com.example.employeemanagement.entity.User;
import com.example.employeemanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import com.example.employeemanagement.entity.Role;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserResponse register(UserRegistrationRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new IllegalArgumentException(
                    "Username already exists"
            );
        }

        User user = new User();

        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(request.getPassword())
        );

        user.setRole(Role.EMPLOYEE);

        User savedUser = userRepository.save(user);

        return new UserResponse(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getRole().name()
        );
    }
}