package com.example.jobMatching.repository;

import com.example.jobMatching.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // Additional query methods if needed
    @Query("SELECT MAX(j.jobId) FROM Job j")
    Long findMaxJobId();
}