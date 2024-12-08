// JobServiceImpl.java
package com.example.jobMatching.service.impl;

import com.example.jobMatching.entity.Job;
import com.example.jobMatching.repository.JobRepository;
import com.example.jobMatching.service.JobService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

    private static final Logger logger = LoggerFactory.getLogger(JobServiceImpl.class);

    // private final JobRepository jobRepository;
    @Autowired
    private JobRepository jobRepository;

    @Autowired
    public JobServiceImpl(JobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    @Override
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @Override
    public Job getJobById(Long id) throws Exception {
        Optional<Job> job = jobRepository.findById(id);
        if (job.isPresent()) {
            return job.get();
        } else {
            throw new Exception("Job not found with ID: " + id);
        }
    }

    @Override
    public Job createJob(Job job) {
        return jobRepository.save(job);
    }

    @Override
    public Job updateJob(Long id, Job jobDetails) {
        logger.debug("Updating job with ID: {}", id);
        logger.debug("Job details: {}", jobDetails);

        // Check if the job exists
        Job existingJob = jobRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Job with ID " + id + " does not exist."));
        if (existingJob != null) {
            // Update the job fields
            existingJob.setTitle(jobDetails.getTitle());
            existingJob.setCategory(jobDetails.getCategory());
            existingJob.setLocation(jobDetails.getLocation());
            existingJob.setDuration(jobDetails.getDuration());
            existingJob.setType(jobDetails.getType());
            existingJob.setSkillsKeyWord(jobDetails.getSkillsKeyWord());
            existingJob.setCompanyId(jobDetails.getCompanyId());
            logger.debug("Existing job details updated: {}", existingJob);
            return jobRepository.save(existingJob);
        } else {
            logger.error("Job with ID {} does not exist.", id);
            throw new IllegalArgumentException("Job with ID " + id + " does not exist.");
        }
    }

    @Override
    public void deleteJob(Long id) {
        if (!jobRepository.existsById(id)) {
            throw new IllegalArgumentException("Job with ID " + id + " does not exist.");
        }

        try {
            jobRepository.deleteById(id);
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete job: " + e.getMessage());
        }
    }

    @Override
    public Long getMaxJobId() {
        return jobRepository.findMaxJobId();
    }

    @Override
    public List<Job> searchJobs(String keyword) {
        logger.debug("Searching for jobs with keyword: {}", keyword);
        return jobRepository.searchJobsByKeyword(keyword.toLowerCase());
    }

    @Override
    public List<Job> searchRemoteJobs(String keyword, boolean remoteOnly) {
        logger.debug("Searching for remote-only jobs with keyword: {}", keyword);
        return jobRepository.searchJobsByKeywordAndType(keyword.toLowerCase(), remoteOnly);
    }

    @Override
    public void performJobTransaction(String title, String category, boolean remoteOnly) {
        jobRepository.performJobTransaction(title, category, remoteOnly);
    }

    @Override
    public Job addJob(Job job) {
        // Save the job to the database; the trigger will handle additional logic
        return jobRepository.save(job);
    }
}