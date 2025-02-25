package com.mavenark.transactions.controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import java.io.File;
import java.io.FileOutputStream;

@RestController
@RequestMapping("/job")
public class JobController {

    private final JobLauncher jobLauncher;
    private final Job excelToMongoJob;

    public JobController(JobLauncher jobLauncher, Job excelToMongoJob) {
        this.jobLauncher = jobLauncher;
        this.excelToMongoJob = excelToMongoJob;
    }

    @PostMapping("/startJob")
    public String startBatchJob(@RequestParam("file") MultipartFile file) {
        try {
            // Save the uploaded file locally
            File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename());
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();

            // Job Parameters
            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", convFile.getAbsolutePath())
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();

            // Start the Job
            jobLauncher.run(excelToMongoJob, jobParameters);
            return "Batch job started successfully!";
        } catch (Exception e) {
            return "Error starting batch job: " + e.getMessage();
        }
    }
}
