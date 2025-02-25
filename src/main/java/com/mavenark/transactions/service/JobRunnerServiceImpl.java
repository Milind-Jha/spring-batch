package com.mavenark.transactions.service;

import com.mavenark.transactions.runner.JobRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class JobRunnerServiceImpl implements JobRunnerService {

    @Autowired
    private JobRunner jobRunner;

    @Override
    public void runBatchJob(String file,long startTime) {
        jobRunner.runBatchJob(file,startTime);
    }
}
