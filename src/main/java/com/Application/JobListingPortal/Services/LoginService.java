package com.Application.JobListingPortal.Services;


import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Application.JobListingPortal.DTO.LoginDto;
import com.Application.JobListingPortal.Entities.UserEntity;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.InvalidCredentialsException;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.UserNotFoundException;
import com.Application.JobListingPortal.JWTAuth.JwtUtil;
import com.Application.JobListingPortal.Repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LoginService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public LoginService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    public String loginUser(LoginDto loginDto) {
        log.info("User login attempt: {}", loginDto.getEmail());

        // Find user by email
        UserEntity user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> {
                    log.warn("Login failed: User not found - {}", loginDto.getEmail());
                    return new UserNotFoundException("User is not registered.");
                });

        // Compare hashed password
        if (!passwordEncoder.matches(loginDto.getPassword(), user.getPassword())) {
            log.warn("Login failed: Invalid credentials for {}", loginDto.getEmail());
            throw new InvalidCredentialsException("Invalid email or password!");
        }

        // Generate JWT Token and Send token to client for authentication in future requests
        return jwtUtil.generateToken(loginDto.getEmail(), user.getRole().toString());
    }
    
}
