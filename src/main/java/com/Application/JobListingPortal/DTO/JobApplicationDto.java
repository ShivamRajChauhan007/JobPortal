package com.Application.JobListingPortal.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JobApplicationDto {
    private Long jobId;
    private String resumeLink;
    private String coverLetterLink;
}
