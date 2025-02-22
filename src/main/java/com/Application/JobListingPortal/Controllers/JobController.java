package com.Application.JobListingPortal.Controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Application.JobListingPortal.DTO.JobCreateRequestDto;
import com.Application.JobListingPortal.DTO.JobDto;
import com.Application.JobListingPortal.Services.JobService;

import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestAttribute;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@Slf4j
@RestController
@RequestMapping("jobListingPortal")
public class JobController {

    final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }
    

    @PostMapping("/create")
    public ResponseEntity<String> createJob(@RequestBody JobDto jobDto,@RequestHeader("Authorization") String token) {        
        log.info("Creating job with details: {}", jobDto);
        Long jobId = jobService.createJob(jobDto, token);
        return ResponseEntity.ok("Job created Successfully with jobID: " + jobId);
    }

    @PutMapping("/update/{jobId}")
    public ResponseEntity<String> updateJob(@PathVariable Long jobId, @RequestBody JobDto jobDto,@RequestHeader("Authorization") String token) {       
        log.info("Updating job ID: {} with details: {}", jobId, jobDto);
        jobService.updateJob(jobId, jobDto, token);
        return ResponseEntity.ok("Updated job with jobId: " + jobId);
    }

    @DeleteMapping("{jobId}")
    public ResponseEntity<String> deleteJob(@PathVariable Long jobId, @RequestHeader("Authorization") String token) {
        log.info("Deleting job ID: {}", jobId);
        jobService.deleteJob(jobId, token);
        return ResponseEntity.ok("Job deleted successfully");
    }

    @GetMapping("my-jobs")
    public ResponseEntity<List<JobDto>> getEmployerJobs(@RequestHeader("Authorization") String token ) {
        log.info("Fetching jobs for employer.");
        return ResponseEntity.ok(jobService.getJobsByEmployer(token));
    }

    /// candidate
    @GetMapping("list")
    public ResponseEntity<Page<JobDto>> listJobs(@RequestParam(required = false) String position,
                                 @RequestParam(required = false) String location,
                                 @RequestParam(required = false) String type,
                                 Pageable pageable) {
        log.info("Listing jobs with filters - Position: {}, Location: {}, Type: {}", position, location, type);
        return ResponseEntity.ok(jobService.getJobs(position, location, type, pageable));
                                
    }

    
}
