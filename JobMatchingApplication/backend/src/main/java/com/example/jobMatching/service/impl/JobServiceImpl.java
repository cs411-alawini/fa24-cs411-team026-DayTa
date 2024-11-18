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

    private final JobRepository jobRepository;

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
    public Job updateJob(Long id, Job jobDetails) throws Exception {
        Job job = getJobById(id);
        job.setTitle(jobDetails.getTitle());
        job.setCompanyId(jobDetails.getCompanyId());
        job.setCategory(jobDetails.getCategory());
        job.setLocation(jobDetails.getLocation());
        job.setDuration(jobDetails.getDuration());
        job.setType(jobDetails.getType());
        job.setSkillsKeyWord(jobDetails.getSkillsKeyWord());
        return jobRepository.save(job);
    }

    @Override
    public void deleteJob(Long id) throws Exception {
        Job job = getJobById(id);
        jobRepository.delete(job);
    }
}