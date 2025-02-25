package com.mavenark.transactions.controller;

import com.mavenark.transactions.runner.JobRunner;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@RestController
@RequestMapping("/batch")
public class JobController {

    private final JobRunner jobRunner;

    @Autowired
    public JobController(JobRunner jobRunner) {
        this.jobRunner = jobRunner;
    }

    @PostMapping("/run-batch")
    public String runJob(@RequestParam("file") MultipartFile file) {
        // Save the file to a specific location (e.g., "C:/input/")
        String filePath = "C:/input/" + file.getOriginalFilename();  // Construct the file path dynamically
        try {
            file.transferTo(new File(filePath));  // Save the file to the specified location
        } catch (IOException e) {
            log.error("Failed to save uploaded file", e);
            return "Failed to save file";
        }

        long startTime = System.currentTimeMillis();
        jobRunner.runBatchJob(filePath, startTime);  // Pass the file path dynamically to run the batch job
        return "Job has started with file: " + filePath;
    }
}
