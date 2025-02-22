package com.Application.JobListingPortal.DTO;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationResponseDto {
    private Long id;
    private Long jobid;
    private String position;
    private String resume;
    private String coverLetter;
    private String status;
    private LocalDateTime applietAt;
}
