// JobServiceImpl.java
package com.example.jobMatching.service.impl;

import com.example.jobMatching.entity.Job;
import com.example.jobMatching.repository.JobRepository;
import com.example.jobMatching.service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class JobServiceImpl implements JobService {

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
        Job existingJob = jobRepository.findById(id).orElse(null);
        if (existingJob != null) {
            // Update fields
            existingJob.setTitle(jobDetails.getTitle());
            existingJob.setCategory(jobDetails.getCategory());
            existingJob.setLocation(jobDetails.getLocation());
            existingJob.setDuration(jobDetails.getDuration());
            existingJob.setType(jobDetails.getType());
            existingJob.setSkillsKeyWord(jobDetails.getSkillsKeyWord());
            existingJob.setCompanyId(jobDetails.getCompanyId());
            return jobRepository.save(existingJob);
        } else {
            // Handle the case where the job doesn't exist
            return null;
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
}