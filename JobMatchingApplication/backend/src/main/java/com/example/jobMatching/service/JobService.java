package com.example.jobMatching.service;

import com.example.jobMatching.entity.Job;

import java.util.List;

public interface JobService {
    Job createJob(Job job);
    List<Job> getAllJobs();
    Job getJobById(Long jobId);
}
