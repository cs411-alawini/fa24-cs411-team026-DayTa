package com.example.jobMatching.repository;

import com.example.jobMatching.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    
    // Call the stored procedure
    // DELIMITER 
    //
    // CREATE PROCEDURE SearchJobsByKeyword(
    //     IN keyword VARCHAR(255)
    // )
    // BEGIN
    //     SELECT * 
    //     FROM job 
    //     WHERE LOWER(title) LIKE CONCAT('%', keyword, '%') 
    //        OR LOWER(category) LIKE CONCAT('%', keyword, '%');
    // END 
    //
    // DELIMITER ;

    @Query(value = "CALL SearchJobsByKeyword(:keyword)", nativeQuery = true)
    List<Job> searchJobsByKeyword(@Param("keyword") String keyword);

    @Query(value = "CALL SearchJobsByKeywordRemote(:keyword)", nativeQuery = true)
    List<Job> searchJobsByKeywordAndType(@Param("keyword") String keyword);

    //create job id
    @Query("SELECT MAX(j.jobId) FROM Job j")
    Long findMaxJobId();
}