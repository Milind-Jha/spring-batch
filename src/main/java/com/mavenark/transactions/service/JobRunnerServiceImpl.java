package com.mavenark.transactions.service;

import com.mavenark.transactions.runner.JobRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobRunnerServiceImpl implements JobRunnerService {

    @Autowired
    private JobRunner jobRunner;

    @Override
    public void runBatchJob(long startTime) {
        jobRunner.runBatchJob(startTime);
    }
}
