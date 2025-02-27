package com.mavenark.transactions.service;

import com.mavenark.transactions.controller.JobController;
import com.mavenark.transactions.runner.JobRunner;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

@Service
@Slf4j
public class JobRunnerServiceImpl implements JobRunnerService {

    @Autowired
    private JobRunner jobRunner;
    @Autowired
    private JobLauncher jobLauncher;
    @Autowired
    private Job excelToMongoJob;

    @Override
    public String runBatchJob(MultipartFile file) {
        if (file == null || file.isEmpty())
            return "Uploaded file is empty";
        if (!Objects.requireNonNull(file.getOriginalFilename()).endsWith(".xlsx"))
            return "Inappropriate file format. The uploaded file must be in .xlsx format.";

        try {
            log.info("File uploaded: {}", file.getOriginalFilename());
            log.info("File size: {} bytes", file.getSize());
            // Save file to a temporary location
            File tempFile = File.createTempFile("upload-", ".xlsx");
            try (FileOutputStream fos = new FileOutputStream(tempFile)) {
                fos.write(file.getBytes());
            }
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("tempFilePath", tempFile.getAbsolutePath())  // Pass file path instead of InputStream
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(excelToMongoJob, jobParameters);
            // Schedule file deletion after some time
            tempFile.deleteOnExit();
            return "Batch job started successfully!";
        } catch (IOException e) {
            log.error("Error processing file: {}", e.getMessage(), e);
            return "Error processing file: " + e.getMessage();
        } catch (Exception e) {
            log.error("Error starting batch job: {}", e.getMessage(), e);
            return "Error starting batch job: " + e.getMessage();
        }
    }
}
