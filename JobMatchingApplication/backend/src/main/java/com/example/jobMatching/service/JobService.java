// JobService.java
package com.example.jobMatching.service;

import com.example.jobMatching.entity.Job;
import java.util.List;

public interface JobService {
    // Existing methods
    List<Job> getAllJobs();
    Job getJobById(Long id) throws Exception;
    Job createJob(Job job);
    Job updateJob(Long id, Job jobDetails);
    void deleteJob(Long id);

    // New method to get the maximum jobId
    Long getMaxJobId();
}