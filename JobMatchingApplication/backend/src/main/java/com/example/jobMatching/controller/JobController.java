// JobController.java
package com.example.jobMatching.controller;

import com.example.jobMatching.entity.Job;
import com.example.jobMatching.service.JobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<?> getJobById(@PathVariable Long id) {
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
            Job createdJob = jobService.createJob(job);
            logger.info("Job created with ID: {}", createdJob.getJobId());
            return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
        } catch (Exception e) {
            logger.error("Failed to create job: {}", e.getMessage());
            return new ResponseEntity<>("Failed to create job", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Update an existing job
    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody Job jobDetails) {
        try {
            Job updatedJob = jobService.updateJob(id, jobDetails);
            return new ResponseEntity<>(updatedJob, HttpStatus.OK);
        } catch (Exception e) {
            logger.error("Failed to update job: {}", e.getMessage());
            return new ResponseEntity<>("Failed to update job", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a job
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteJob(@PathVariable Long id) {
        try {
            jobService.deleteJob(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            logger.error("Failed to delete job: {}", e.getMessage());
            return new ResponseEntity<>("Failed to delete job", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}