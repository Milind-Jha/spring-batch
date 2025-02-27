package com.mavenark.transactions.job;

import com.mavenark.transactions.dto.EmployeeDTO;
import com.mavenark.transactions.mapper.EmployeeFileRowMapper;
import com.mavenark.transactions.document.Employee;
import com.mavenark.transactions.processor.EmployeeProcessor;
import com.mavenark.transactions.reader.ExcelItemReader;
import com.mavenark.transactions.repo.EmployeeRepo;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;

@Configuration
@EnableBatchProcessing
public class JobConfiguration {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final EmployeeProcessor employeeProcessor;
    private final EmployeeRepo employeeRepo;

    public JobConfiguration(JobRepository jobRepository, PlatformTransactionManager transactionManager,
                            EmployeeProcessor employeeProcessor, EmployeeRepo employeeRepo) {
        this.jobRepository = jobRepository;
        this.transactionManager = transactionManager;
        this.employeeProcessor = employeeProcessor;
        this.employeeRepo = employeeRepo;
    }

    @Bean
    public Job excelToMongoJob(Step step1) {
        return new JobBuilder("excelToMongoJob", jobRepository)
                .start(step1)
                .build();
    }

    @Bean
    public Step step1(ExcelItemReader<EmployeeDTO> employeeReader) {
        return new StepBuilder("excelToMongoStep", jobRepository)
                .<EmployeeDTO, Employee>chunk(500, transactionManager)
                .reader(employeeReader)
                .processor(employeeProcessor)
                .writer(employeeWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public ExcelItemReader<EmployeeDTO> employeeReader(@Value("#{jobParameters['tempFilePath']}") String tempFilePath) throws Exception {
        Resource fileResource = new FileSystemResource(tempFilePath); // Correct type
        return new ExcelItemReader<>(fileResource, new EmployeeFileRowMapper()); // Pass Resource instead of InputStream
    }

    @Bean
    public RepositoryItemWriter<Employee> employeeWriter() {
        RepositoryItemWriter<Employee> writer = new RepositoryItemWriter<>();
        writer.setRepository(employeeRepo);
        writer.setMethodName("save");
        return writer;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor();
        taskExecutor.setConcurrencyLimit(16);
        return taskExecutor;
    }
}
