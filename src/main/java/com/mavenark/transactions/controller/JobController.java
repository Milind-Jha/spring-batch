package com.mavenark.transactions.controller;

import com.mavenark.transactions.service.JobRunnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class JobController {

    @Autowired
    private JobRunnerService jobRunnerService;

    @RequestMapping(value = "/upload")
    public String runJob() {
        long startTime = System.currentTimeMillis();
        jobRunnerService.runBatchJob(startTime);
        return "Job initiation started";
    }
}
