package com.Application.JobListingPortal.DTO;

import com.Application.JobListingPortal.Entities.UserEntity.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterDto {
    private String name;
    private String email;
    private String password;
    private String role;
}
