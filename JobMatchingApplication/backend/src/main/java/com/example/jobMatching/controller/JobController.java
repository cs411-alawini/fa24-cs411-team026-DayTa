// JobController.java
package com.example.jobMatching.controller;

import com.example.jobMatching.entity.Job;
import com.example.jobMatching.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final JobService jobService;
    private static final Logger logger = LoggerFactory.getLogger(JobController.class);

    @Autowired
    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    // Get all jobs
    @GetMapping
    public ResponseEntity<?> getAllJobs() {
        try {
            return new ResponseEntity<>(jobService.getAllJobs(), HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to fetch jobs: {}", e.getMessage());
            return new ResponseEntity<>("Failed to fetch jobs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get job by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getJobById(@PathVariable("id") Long id) {
        try {
            Job job = jobService.getJobById(id);
            return new ResponseEntity<>(job, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Job not found: {}", e.getMessage());
            return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
        }
    }

    // Create a new job
    @PostMapping
    public ResponseEntity<?> createJob(@RequestBody Job job) {
        try {
            // Generate a unique jobId
            Long newJobId = generateUniqueJobId();
            job.setJobId(newJobId);

            Job createdJob = jobService.createJob(job);
            logger.info("Job created with ID: {}", createdJob.getJobId());
            return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to create job: {}", e.getMessage());
            return new ResponseEntity<>("Failed to create job", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private Long generateUniqueJobId() {
        Long maxJobId = jobService.getMaxJobId();
        if (maxJobId == null) {
            return 1L;
        } else {
            return maxJobId + 1;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable("id") Long id, @RequestBody Job jobDetails) {
        try {
            Job updatedJob = jobService.updateJob(id, jobDetails);
            if (updatedJob != null) {
                return new ResponseEntity<>(updatedJob, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Job not found", HttpStatus.NOT_FOUND);
            }
        } catch (IllegalArgumentException e) {
            logger.error("Job not found: {}", e.getMessage());
            return new ResponseEntity<>("Job not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            logger.error("Failed to update job: {}", e.getMessage());
            return new ResponseEntity<>("Failed to update job", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a job
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable("id") Long id) {
        try {
            jobService.deleteJob(id);
            logger.info("Successfully deleted job with ID: {}", id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            logger.error("Job not found: {}", e.getMessage());
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (RuntimeException e) {
            logger.error("Failed to delete job: {}", e.getMessage());
            return new ResponseEntity<>("Failed to delete job", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchJobs(@RequestParam("keyword") String keyword,
                                        @RequestParam(value = "remoteOnly", required = false, defaultValue = "false") boolean remoteOnly) {
        try {
            List<Job> jobs;
            jobs = jobService.searchRemoteJobs(keyword, remoteOnly);
            return new ResponseEntity<>(jobs, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to search jobs: {}", e.getMessage());
            return new ResponseEntity<>("Failed to search jobs", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
}