package com.mavenark.transactions.writer;

import com.mavenark.transactions.document.Employee;
import com.mavenark.transactions.repo.EmployeeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class EmployeeDBWriter implements ItemWriter<Employee> {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    public void write(List<? extends Employee> employees) {
        if (employees == null || employees.isEmpty()) {
            log.info("No employees to write to MongoDB.");
            return;
        }

        log.info("Saving {} employees to MongoDB.", employees.size());

        employeeRepo.saveAll(employees); // MongoDB batch insert
        log.info("Saved {} employees to MongoDB.", employees.size());
    }
}
