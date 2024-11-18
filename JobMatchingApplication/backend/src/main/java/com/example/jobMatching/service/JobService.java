// JobService.java
package com.example.jobMatching.service;

import com.example.jobMatching.entity.Job;
import java.util.List;

public interface JobService {
    List<Job> getAllJobs();
    Job getJobById(Long id) throws Exception;
    Job createJob(Job job);
    Job updateJob(Long id, Job jobDetails) throws Exception;
    void deleteJob(Long id) throws Exception;
}