package com.Application.JobListingPortal.Services;

import java.time.LocalDateTime;

import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Application.JobListingPortal.DTO.RegisterDto;
import com.Application.JobListingPortal.Entities.UserEntity;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.InvalidRoleException;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.UserAlreadyExistsException;
import com.Application.JobListingPortal.Repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RegistrationService {

    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final BCryptPasswordEncoder passwordEncoder;

    public RegistrationService(UserRepository userRepository, ModelMapper modelMapper,BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public String registerUser(RegisterDto registerDto){
        log.info("Registering new user: {}", registerDto.getEmail());

        // Check if the user already exists
        if (userRepository.findByEmail(registerDto.getEmail()).isPresent()) {
            log.warn("User already registered: {}", registerDto.getEmail());
            throw new UserAlreadyExistsException("User already registered with this email!");
        }

        // Validate role
        if (!isValidRole(registerDto.getRole())) {
            log.warn("Invalid role provided: {}", registerDto.getRole());
            throw new InvalidRoleException("Invalid role! Role must be EMPLOYER or JOB_SEEKER.");
        }

        // Map DTO to Entity
        UserEntity userEntity = modelMapper.map(registerDto, UserEntity.class);
        userEntity.setPassword(passwordEncoder.encode(registerDto.getPassword()));
        userEntity.setCreatedAt(LocalDateTime.now());
        userEntity.setRole(UserEntity.Role.valueOf(registerDto.getRole().toUpperCase()));

        // Save user
        userRepository.save(userEntity);
        log.info("User registered successfully: {}", userEntity.getEmail());

        return "User Registered Successfully!";
    } 

    private boolean isValidRole(String role) {
        try {
            UserEntity.Role.valueOf(role); // Convert String to Enum
            return true;
        } catch (IllegalArgumentException e) {
            return false; // If conversion fails, role is invalid
        }
    }
}
