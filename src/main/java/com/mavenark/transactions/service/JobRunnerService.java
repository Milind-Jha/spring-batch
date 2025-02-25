package com.mavenark.transactions.service;

import org.springframework.web.multipart.MultipartFile;

public interface JobRunnerService {
    void runBatchJob(String file, long startTime);
}
