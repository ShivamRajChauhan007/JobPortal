package com.Application.JobListingPortal.Services;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.Application.JobListingPortal.DTO.JobDto;
import com.Application.JobListingPortal.Entities.JobEntity;
import com.Application.JobListingPortal.Entities.UserEntity;
import com.Application.JobListingPortal.JWTAuth.JwtUtil;
import com.Application.JobListingPortal.Entities.JobEntity.JobType;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.InvalidJobTypeException;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.JobNotFoundException;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.UnauthorizedActionException;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.UserNotFoundException;
import com.Application.JobListingPortal.Repositories.JobRepository;
import com.Application.JobListingPortal.Repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobService {
    
    private final ModelMapper modelMapper;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public JobService(ModelMapper modelMapper, JobRepository jobRepository, UserRepository userRepository, JwtUtil jwtUtil) {
        this.modelMapper = modelMapper;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
    }

    public Long createJob(JobDto jobDto, String token) {
        log.info("Attempting to create a job...");
        
        // Extract email from JWT token
        String email = jwtUtil.extractEmail(token);
    
        // Find the employer using email
        UserEntity employer = userRepository.findByEmail(email)
            .orElseThrow(() -> {
                log.error("Employer not found: {}", email);
                return new UserNotFoundException("Employer not found");
            });

        // Map DTO to Entity
        JobEntity job = modelMapper.map(jobDto, JobEntity.class);
        
        // Set additional fields
        job.setCreatedAt(LocalDateTime.now());
        job.setEmployer(employer); // Set the employer who is creating the job
    
        try {
            job.setJobType(JobEntity.JobType.valueOf(jobDto.getJobType().toUpperCase())); 
        } catch (IllegalArgumentException e) {
            throw new InvalidJobTypeException("Invalid job type: " + jobDto.getJobType());
        }
        // Save the job
        job.setSkills(job.getSkills());
        JobEntity savedJob = jobRepository.save(job);
        log.info("Job created successfully with ID: {}", savedJob.getId());
        return savedJob.getId(); // Return the created job's ID
    }
    

    

    public JobDto updateJob(Long jobId, JobDto jobRequest, String token) {
        log.info("Attempting to update job ID: {}", jobId);

        String email = jwtUtil.extractEmail(token);
    
        // Find employer using email
        UserEntity employer = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Employer not found with email: {}", email);
                    return new UserNotFoundException("Employer not found");
                });
    
        // Find the job by ID and check if it belongs to the employer
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> {
                    log.error("Job not found with ID: {}", jobId);
                    return new JobNotFoundException("Job not found");
                });
    
        if (!job.getEmployer().getId().equals(employer.getId())) {
            log.warn("Unauthorized update attempt by employer ID: {}", employer.getId());
            throw new UnauthorizedActionException("Unauthorized: You can only update your own job listings");
        }
    
        // Update job details
        job.setPosition(jobRequest.getPosition());
        job.setDescription(jobRequest.getDescription());
        job.setSkills(jobRequest.getSkills());
        job.setLocation(jobRequest.getLocation());
    
        // Convert String to Enum safely
        if (jobRequest.getJobType() != null) {
            try {
                job.setJobType(JobType.valueOf(jobRequest.getJobType().toUpperCase())); // Convert to uppercase to match enum
            } catch (IllegalArgumentException e) {
                throw new InvalidJobTypeException("Invalid job type: " + jobRequest.getJobType());
            }
        }
        job.setSkills(job.getSkills());
    
        // Save updated job
        JobEntity updatedJob = jobRepository.save(job);
        log.info("Job ID: {} updated successfully", jobId);
        // Convert to DTO and return
        return modelMapper.map(updatedJob, JobDto.class);
    }
    



    public void deleteJob(Long jobId,String token) {
        log.info("Attempting to delete job ID: {}", jobId);
        String email = jwtUtil.extractEmail(token);
    
        // Find employer using email
        UserEntity employer = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Employer not found with email: {}", email);
                    return new RuntimeException("Employer not found");
                });

        // Find the job by ID and check if it belongs to the employer
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> {
                    log.error("Job not found with ID: {}", jobId);
                    return new JobNotFoundException("Job not found");
                });

        if (!job.getEmployer().getId().equals(employer.getId())) {
            log.warn("Unauthorized delete attempt by employer ID: {}", employer.getId());
            throw new UnauthorizedActionException("Unauthorized: You can only delete your own job listings");
        }

        // If the employer owns the job, delete it
        jobRepository.delete(job);
        log.info("Job ID: {} deleted successfully", jobId);
    }

    public List<JobDto> getJobsByEmployer(String token){
        String email = jwtUtil.extractEmail(token);
        log.info("Fetching jobs for employer with email: {}", email);

        UserEntity employer = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("Employer not found with email: {}", email);
                    return new UserNotFoundException("Employer not found");
                });
        
        List<JobEntity> jobs = jobRepository.findByEmployer(employer);
        log.info("Found {} jobs for employer ID: {}", jobs.size(), employer.getId());

        return jobs.stream()
                   .map(job -> modelMapper.map(job, JobDto.class))
                   .toList();
    }
    
    public Page<JobDto> getJobs(String position, String location, String type, Pageable pageable) {
        log.info("Fetching jobs with filters - Position: {}, Location: {}, Type: {}", position, location, type);

        JobEntity.JobType jobType = null;

        
        // Convert string to JobType Enum if provided
        if (type != null && !type.isEmpty()) {
            try {
                jobType = JobType.valueOf(type.toUpperCase());
            } catch (IllegalArgumentException e) {
                log.warn("Invalid job type provided: {}", type);
                throw new InvalidJobTypeException("Invalid job type: " + type);
            }
        }

        // Fetch jobs with filters
        Page<JobEntity> jobPage = jobRepository.findJobs(position, location, jobType, pageable);

        log.info("Found {} jobs matching criteria", jobPage.getTotalElements());

        // Convert Job entities to JobDto
        return jobPage.map(job -> modelMapper.map(job, JobDto.class));
    }

}
