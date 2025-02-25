package com.mavenark.transactions.job;

import com.mavenark.transactions.dto.EmployeeDTO;
import com.mavenark.transactions.mapper.EmployeeFileRowMapper;
import com.mavenark.transactions.model.Employee;
import com.mavenark.transactions.processor.EmployeeProcessor;
import com.mavenark.transactions.reader.ExcelItemReader;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import javax.sql.DataSource;

@Configuration
public class Demo4 {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final EmployeeProcessor employeeProcessor;
    private final DataSource dataSource;

    @Autowired
    public Demo4(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, EmployeeProcessor employeeProcessor, DataSource dataSource) {
        this.jobBuilderFactory = jobBuilderFactory;
        this.stepBuilderFactory = stepBuilderFactory;
        this.employeeProcessor = employeeProcessor;
        this.dataSource = dataSource;
    }

    @Qualifier(value = "demo4")
    @Bean
    public Job demo4Job() throws Exception {
        return this.jobBuilderFactory.get("demo4")
                .start(step1Demo4())
                .build();
    }

    @Bean
    public Step step1Demo4() throws Exception {
        return this.stepBuilderFactory.get("step1")
                .<EmployeeDTO, Employee>chunk(500)
                .reader(employeeReader(null))  // File path will be passed dynamically here
                .processor(employeeProcessor)
                .writer(employeeDBWriterDefault())
                .taskExecutor(taskExecutor())
                .build();
    }

    @Bean
    @StepScope
    public ExcelItemReader<EmployeeDTO> employeeReader(@Value("#{jobParameters['filePath']}") String filePath) throws Exception {
        Resource fileResource = new FileSystemResource(filePath);  // Use FileSystemResource for dynamic file location
        return new ExcelItemReader<>(fileResource, new EmployeeFileRowMapper());
    }

    @Bean
    public JdbcBatchItemWriter<Employee> employeeDBWriterDefault() {
        JdbcBatchItemWriter<Employee> itemWriter = new JdbcBatchItemWriter<>();
        itemWriter.setDataSource(dataSource);
        itemWriter.setSql("INSERT INTO employee (employee_id, first_name, last_name, email, age) " +
                "VALUES (:employeeId, :firstName, :lastName, :email, :age)");
        itemWriter.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
        return itemWriter;
    }

    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor simpleAsyncTaskExecutor = new SimpleAsyncTaskExecutor();
        simpleAsyncTaskExecutor.setConcurrencyLimit(16);
        return simpleAsyncTaskExecutor;
    }
}
