package com.example.jobMatching.repository;

import com.example.jobMatching.entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value = "CALL SearchJobsByKeywordType(:keyword, :remoteOnly)", nativeQuery = true)
    List<Job> searchJobsByKeywordAndType(@Param("keyword") String keyword, @Param("remoteOnly") boolean remoteOnly);
    
    //create job id
    @Query("SELECT MAX(j.jobId) FROM Job j")
    Long findMaxJobId();

    @Modifying
    @Query(value = "CALL PerformJobTransaction(:title, :category, :remoteOnly)", nativeQuery = true)
    void performJobTransaction(@Param("title") String title, 
                               @Param("category") String category, 
                               @Param("remoteOnly") boolean remoteOnly);
    
                               
}
