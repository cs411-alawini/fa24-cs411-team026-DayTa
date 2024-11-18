package com.example.jobMatching.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/jobs")
    public String getJobsPage() {
        // Forward to the static index.html located in src/main/resources/static/frontend
        return "forward:/frontend/index.html";
    }
}