package com.mavenark.transactions.writer;

import com.mavenark.transactions.document.Employee;
import com.mavenark.transactions.repo.EmployeeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class EmployeeDBWriter implements ItemWriter<Employee> {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public void write(Chunk<? extends Employee> employeesChunk) {
        if (employeesChunk == null || employeesChunk.isEmpty()) {
            log.info("No employees to write to MongoDB.");
            return;
        }

        log.info("Saving {} employees to MongoDB.", employeesChunk.size());

        employeeRepo.saveAll(employeesChunk.getItems()); // MongoDB batch insert
        log.info("Saved {} employees to MongoDB.", employeesChunk.size());
    }
}
