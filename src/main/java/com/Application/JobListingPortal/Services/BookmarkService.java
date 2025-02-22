package com.Application.JobListingPortal.Services;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.Application.JobListingPortal.DTO.JobDto;
import com.Application.JobListingPortal.Entities.BookmarkEntity;
import com.Application.JobListingPortal.Entities.JobEntity;
import com.Application.JobListingPortal.Entities.UserEntity;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.DuplicateBookmarkException;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.JobNotFoundException;
import com.Application.JobListingPortal.Exceptions.CustomExceptions.UserNotFoundException;
import com.Application.JobListingPortal.JWTAuth.JwtUtil;
import com.Application.JobListingPortal.Repositories.BookmarkRepository;
import com.Application.JobListingPortal.Repositories.JobRepository;
import com.Application.JobListingPortal.Repositories.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final JobRepository jobRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    final ModelMapper modelMapper;

    public BookmarkService(BookmarkRepository bookmarkRepository, JobRepository jobRepository, UserRepository userRepository, JwtUtil jwtUtil, ModelMapper modelMapper) {
        this.bookmarkRepository = bookmarkRepository;
        this.jobRepository = jobRepository;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.modelMapper = modelMapper;
    }
   
    
    public String bookmarkJob(Long jobId, String token) {
        log.info("Attempting to bookmark job ID: {}", jobId);

        // Extract user email from token
        String userEmail = jwtUtil.extractEmail(token);
        log.debug("Extracted email from token: {}", userEmail);
        
        // Find user
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userEmail);
                    return new UserNotFoundException("User not found");
                });
        
        // Check if user is a JOB_SEEKER
        /*if (user.getRole() != UserEntity.Role.JOB_SEEKER) {
            throw new RuntimeException("Only job seekers can bookmark jobs");
        }*/

        // Find job
        JobEntity job = jobRepository.findById(jobId)
                .orElseThrow(() -> {
                    log.error("Job not found: {}", jobId);
                    return new JobNotFoundException("Job not found");
                });

        // Check if the job is already bookmarked
        if (bookmarkRepository.findByUserAndJob(user, job).isPresent()) {
            log.warn("User {} already bookmarked job {}", userEmail, jobId);
            throw new DuplicateBookmarkException("Job already bookmarked");
        }

        // Save bookmark
        BookmarkEntity bookmark = new BookmarkEntity();
        bookmark.setUser(user);
        bookmark.setJob(job);
        bookmarkRepository.save(bookmark);

        log.info("Job ID {} bookmarked successfully by user {}", jobId, userEmail);
        return "Job bookmarked successfully!";
    }

    public List<JobDto> getBookmarkedJobs(String token) {
        log.info("Fetching bookmarked jobs for user");

        // Extract user email from token
        String userEmail = jwtUtil.extractEmail(token);
        log.debug("Extracted email from token: {}", userEmail);
        
        // Find user
        UserEntity user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> {
                    log.error("User not found: {}", userEmail);
                    return new UserNotFoundException("User not found");
                });

        // Check if user is a JOB_SEEKER
        // if (user.getRole() != UserEntity.Role.JOB_SEEKER) {
        //     throw new RuntimeException("Only job seekers can view bookmarked jobs");
        // }

        // Get all bookmarks for the user
        List<BookmarkEntity> bookmarks = bookmarkRepository.findByUser(user);

        // Convert jobs to DTOs
        List<JobDto> jobDtos = bookmarks.stream()
                .map(bookmark -> modelMapper.map(bookmark.getJob(), JobDto.class))
                .collect(Collectors.toList());

        log.info("Retrieved {} bookmarked jobs for user {}", jobDtos.size(), userEmail);
        return jobDtos;
    }
}
