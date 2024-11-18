package com.example.jobMatching.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;

    @Column(nullable = false)
    private Long companyId;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String category;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Integer duration;

    @Column(nullable = false)
    private String type;

    @Column(name = "skills_keyword", nullable = false, length = 500)
    private String skillsKeyWord;

    // No-Args Constructor
    public Job() {
    }

    // All-Args Constructor
    public Job(Long jobId, Long companyId, String title, String category, String location, Integer duration, String type, String skillsKeyWord) {
        this.jobId = jobId;
        this.companyId = companyId;
        this.title = title;
        this.category = category;
        this.location = location;
        this.duration = duration;
        this.type = type;
        this.skillsKeyWord = skillsKeyWord;
    }

    // Getters and Setters

    public Long getJobId() {
        return jobId;
    }

    public void setJobId(Long jobId) {
        this.jobId = jobId;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getDuration() {
        return duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
    
    public String getSkillsKeyWord() {
        return skillsKeyWord;
    }

    public void setSkillsKeyWord(String skillsKeyWord) {
        this.skillsKeyWord = skillsKeyWord;
    }
}