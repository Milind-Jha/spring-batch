package com.mavenark.transactions.controller;

import com.mavenark.transactions.service.JobRunnerService;
import org.apache.coyote.BadRequestException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;

@RestController
@RequestMapping("transaction/job")
public class JobController {

    @Autowired
    private JobRunnerService jobRunnerService;

    @PostMapping("/stockMasterV1")
    public String startBatchJob(@RequestParam("file") MultipartFile file) {
        return jobRunnerService.runBatchJob(file);
    }
}
