package com.mavenark.transactions.job;

import com.mavenark.transactions.dto.EmployeeDTO;
import com.mavenark.transactions.mapper.EmployeeFileRowMapper;
import com.mavenark.transactions.document.Employee;
import com.mavenark.transactions.processor.EmployeeProcessor;
import com.mavenark.transactions.reader.ExcelItemReader;
import com.mavenark.transactions.repo.EmployeeRepo;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.data.RepositoryItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

@Configuration
public class JobConfiguration {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EmployeeProcessor employeeProcessor;
    private final EmployeeRepo employeeRepo;

    @Autowired
    public JobConfiguration(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory,
                            EmployeeProcessor employeeProcessor, EmployeeRepo employeeRepo) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.employeeProcessor = employeeProcessor;
        this.employeeRepo = employeeRepo;
    }

    @Qualifier("excelToMongoJob")
    @Bean
    public Job excelToMongoJob() throws Exception {
        return jobBuilderFactory.get("excelToMongoJob")
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() throws Exception {
        return stepBuilderFactory.get("excelToMongoStep")
                .<EmployeeDTO, Employee>chunk(500)
                .reader(employeeReader(null))
                .processor(employeeProcessor)
                .writer(employeeWriter())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public ExcelItemReader<EmployeeDTO> employeeReader(@Value("#{jobParameters['filePath']}") String filePath) throws Exception {
        Resource fileResource = new FileSystemResource(filePath);
        return new ExcelItemReader<>(fileResource, new EmployeeFileRowMapper());
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
