package com.Application.JobListingPortal.Controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Application.JobListingPortal.DTO.JobDto;
import com.Application.JobListingPortal.Services.BookmarkService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("jobListingPortal")
public class JobBookmarkController {

    final BookmarkService bookmarkService;

    public JobBookmarkController(BookmarkService bookmarkService) {
        this.bookmarkService = bookmarkService;
    }
    
    @PostMapping("/bookmark/{jobId}")
    public ResponseEntity<String> bookmarkJob(@PathVariable Long jobId, @RequestHeader("Authorization") String token) {
        log.info("Received request to bookmark job ID: {}", jobId);
        String response = bookmarkService.bookmarkJob(jobId, token);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-bookmarks")
    public ResponseEntity<List<JobDto>> getBookmarkedJobs( @RequestHeader("Authorization") String token) {
        log.info("Fetching bookmarked jobs for user with token.");
        List<JobDto> bookmarkedJobs = bookmarkService.getBookmarkedJobs(token);
        return ResponseEntity.ok(bookmarkedJobs);
    }
    
}
