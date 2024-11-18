package com.example.jobMatching.repository;

import com.example.jobMatching.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    // Additional query methods (if any) can be defined here
    
}
