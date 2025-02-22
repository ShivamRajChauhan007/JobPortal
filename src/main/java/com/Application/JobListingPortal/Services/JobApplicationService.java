package com.Application.JobListingPortal.Services;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.Application.JobListingPortal.DTO.JobApplicationDto;
import com.Application.JobListingPortal.DTO.JobApplicationResponseDto;
import com.Application.JobListingPortal.Entities.ApplicationEntity;
import com.Application.JobListingPortal.Entities.JobEntity;
import com.Application.JobListingPortal.Entities.UserEntity;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.DuplicateApplicationException;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.JobNotFoundException;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.UnauthorizedAccessException;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.UserNotFoundException;
import com.Application.JobListingPortal.JWTAuth.JwtUtil;
import com.Application.JobListingPortal.Repositories.ApplicationRepository;
import com.Application.JobListingPortal.Repositories.JobRepository;
import com.Application.JobListingPortal.Repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JobApplicationService {


    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final ApplicationRepository applicationRepository;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    public JobApplicationService(JobRepository jobRepository, UserRepository userRepository, ApplicationRepository applicationRepository, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.applicationRepository = applicationRepository;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }
    

    public JobApplicationResponseDto applyForJob(JobApplicationDto jobApplicationDto, String token) {
        log.info("Applying for job with ID: {}", jobApplicationDto.getJobId());

        // Extract email from JWT token
        String email = jwtUtil.extractEmail(token);
        log.debug("Extracted email from token: {}", email);

        // Find the applicant using email
        UserEntity applicant = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    log.error("User not found: {}", email);
                    return new UserNotFoundException("User not found");
                });

        // Find the job using jobId
        JobEntity job = jobRepository.findById(jobApplicationDto.getJobId())
                .orElseThrow(() -> {
                    log.error("Job not found: {}", jobApplicationDto.getJobId());
                    return new JobNotFoundException("Job not found");
                });

       // Check if user has already applied
        if (applicationRepository.existsByJobAndApplicant(job, applicant)) {
            log.warn("User {} has already applied for job {}", email, jobApplicationDto.getJobId());
            throw new DuplicateApplicationException("You have already applied for this job!");
        }

        // Create ApplicationEntity object
        ApplicationEntity application = new ApplicationEntity();
        application.setJob(job);
        application.setApplicant(applicant);
        application.setResume(jobApplicationDto.getResumeLink());
        application.setCoverLetter(jobApplicationDto.getCoverLetterLink());
        application.setStatus(ApplicationEntity.Applicationstatus.PENDING);
        application.setAppliedAt(LocalDateTime.now());

        // Save application
        applicationRepository.save(application);
        log.info("Application submitted successfully for job {} by {}", jobApplicationDto.getJobId(), email);

        // Convert to DTO and return
        return new JobApplicationResponseDto(
                application.getId(),
                job.getId(),
                job.getPosition(),
                application.getResume(),
                application.getCoverLetter(),
                application.getStatus().toString(),
                application.getAppliedAt()
        );
         
    }


    public List<JobApplicationResponseDto> listMyApplications(String token) {
        // Extract user email from JWT token
        String email = jwtUtil.extractEmail(token);
        log.debug("Listing applications for user: {}", email);


        UserEntity applicant = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        // Fetch applications by the user
        List<ApplicationEntity> applications = applicationRepository.findByApplicant(applicant);

        // Convert to DTO
        return applications.stream()
                .map(app -> new JobApplicationResponseDto(
                        app.getId(),
                        app.getJob().getId(), 
                        app.getJob().getPosition(),
                        app.getResume(), 
                        app.getCoverLetter(), 
                        app.getStatus().toString(), 
                        app.getAppliedAt()))
                .collect(Collectors.toList());
    }

    public List<JobApplicationResponseDto> listApplicationsByJobId(Long jobId, String token) {
        // Extract employer email from JWT token
        String email = jwtUtil.extractEmail(token);
        log.debug("Fetching applications for job {} by employer {}", jobId, email);
    
        // Find employer
        UserEntity employer = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("Employer not found"));
    
        // Find job and verify employer
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> new JobNotFoundException("Job not found"));
    
        if (!job.getEmployer().getId().equals(employer.getId())) {
            log.error("Unauthorized access: {} tried to view applications for job {}", email, jobId);
            throw new UnauthorizedAccessException("You are not the owner of this job!");
        }
    
        // Fetch applications for this job
        List<ApplicationEntity> applications = applicationRepository.findByJob(job);
    
        // Convert to DTO
        return applications.stream()
                .map(app -> new JobApplicationResponseDto(
                        app.getId(), 
                        jobId, 
                        job.getPosition(),
                        app.getResume(), 
                        app.getCoverLetter(), 
                        app.getStatus().toString(), 
                        app.getAppliedAt()))
                .collect(Collectors.toList());
    }
    
}
