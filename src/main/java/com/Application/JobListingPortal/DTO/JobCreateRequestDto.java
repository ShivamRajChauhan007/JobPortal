package com.Application.JobListingPortal.DTO;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobCreateRequestDto {
    private String position;
    private String description;
    private List<String> skills;
    private String location;
    private String type; // FULL_TIME, INTERN, CONTRACT
    private String organisation;
    private String noOfVacencies;
}
