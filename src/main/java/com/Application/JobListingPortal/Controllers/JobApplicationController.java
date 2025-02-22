package com.Application.JobListingPortal.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Application.JobListingPortal.DTO.JobApplicationDto;
import com.Application.JobListingPortal.DTO.JobApplicationResponseDto;
import com.Application.JobListingPortal.Services.JobApplicationService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("jobListingpPortal/")
public class JobApplicationController {
    private final JobApplicationService jobApplicationService;

    public JobApplicationController(JobApplicationService jobApplicationService) {
        this.jobApplicationService = jobApplicationService;
    }

    @PostMapping("/apply")
    public JobApplicationResponseDto applyForJob(@RequestBody JobApplicationDto jobApplication, @RequestHeader("Authorization") String token) {
        JobApplicationResponseDto response = jobApplicationService.applyForJob(jobApplication, token);  
        log.info("Applied for the job successfully: {}", response.getId()); 
        return response;      
    }

    @GetMapping("/my-applications")
    public List<JobApplicationResponseDto> listMyApplications(@RequestHeader("Authorization") String token) {
        return jobApplicationService.listMyApplications(token);
    }


    @GetMapping("/job/{jobId}/applications")
    public List<JobApplicationResponseDto> listApplicationsByJobId(@PathVariable Long jobId, @RequestHeader("Authorization") String token) {
        return jobApplicationService.listApplicationsByJobId(jobId, token);
    }

    
    
}
