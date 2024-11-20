package com.example.jobMatching.repository;

import com.example.jobMatching.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    //search bar
    @Query("SELECT j FROM Job j WHERE " +
       "j.title LIKE CONCAT('%', :keyword, '%') OR " +
       "j.category LIKE CONCAT('%', :keyword, '%')")
    List<Job> searchJobsByKeyword(@Param("keyword") String keyword);
    
    //create job id
    @Query("SELECT MAX(j.jobId) FROM Job j")
    Long findMaxJobId();
}