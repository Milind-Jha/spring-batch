package com.mavenark.transactions.service;

import org.springframework.web.multipart.MultipartFile;

public interface JobRunnerService {
    String runBatchJob(MultipartFile file);
}
