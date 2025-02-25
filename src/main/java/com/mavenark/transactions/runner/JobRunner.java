package com.mavenark.transactions.runner;

import com.mavenark.transactions.utils.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
@Slf4j
public class JobRunner {

    private final JobLauncher jobLauncher;
    private final Job demoJob;

    @Autowired
    public JobRunner(Job demoJob, JobLauncher jobLauncher) {
        this.jobLauncher = jobLauncher;
        this.demoJob = demoJob;
    }

    @Async
    public void runBatchJob(long startTime) {
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();
        jobParametersBuilder.addString(Constants.FILE_NAME_CONTEXT_KEY, "employees.xlsx");
        jobParametersBuilder.addDate("date", new Date(), true);
        log.info("Running batch job with file: {}", "employees.xlsx");
        runJob(demoJob, jobParametersBuilder.toJobParameters(),startTime);
    }

    public void runJob(Job job, JobParameters parameters,long startTime) {
        try {
            log.info("Starting job execution for file: {}", parameters.getString(Constants.FILE_NAME_CONTEXT_KEY, "N/A"));
            JobExecution jobExecution = jobLauncher.run(job, parameters);
            log.info("Job execution status: {} {}", jobExecution.getStatus(),(System.currentTimeMillis() - startTime));
        } catch (JobExecutionAlreadyRunningException e) {
            log.warn("Job is already running for file: {}", parameters.getString(Constants.FILE_NAME_CONTEXT_KEY, "N/A"));
        } catch (JobRestartException e) {
            log.warn("Job restart failed for file: {}", parameters.getString(Constants.FILE_NAME_CONTEXT_KEY, "N/A"));
        } catch (JobInstanceAlreadyCompleteException e) {
            log.warn("Job already completed for file: {}", parameters.getString(Constants.FILE_NAME_CONTEXT_KEY, "N/A"));
        } catch (JobParametersInvalidException e) {
            log.error("Invalid job parameters for file: {}", parameters.getString(Constants.FILE_NAME_CONTEXT_KEY, "N/A"), e);
        }
    }
}
