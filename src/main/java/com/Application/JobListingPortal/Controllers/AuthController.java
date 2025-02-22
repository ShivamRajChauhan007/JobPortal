package com.Application.JobListingPortal.Controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Application.JobListingPortal.DTO.LoginDto;
import com.Application.JobListingPortal.DTO.RegisterDto;

import org.springframework.web.bind.annotation.PostMapping;

import com.Application.JobListingPortal.Services.LoginService;
import com.Application.JobListingPortal.Services.RegistrationService;

import lombok.extern.slf4j.Slf4j;


@RestController
@Slf4j
@RequestMapping("jobListingpPortal")
public class AuthController {
    

    final RegistrationService registrationService;
    final LoginService loginService;

    public AuthController(RegistrationService registrationService, LoginService loginService) {
        this.registrationService = registrationService;
        this.loginService = loginService;
    }

    @PostMapping("/register") 
    public ResponseEntity<String> register(@RequestBody RegisterDto registerDto){
        String response = registrationService.registerUser(registerDto);
        log.info("User registered successfully: {}", registerDto.getEmail()); 
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<String> postMethodName(@RequestBody LoginDto loginDto) {
        String token = loginService.loginUser(loginDto);
        return ResponseEntity.ok(token);
    }
    
     
}
