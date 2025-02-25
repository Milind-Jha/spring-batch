package com.mavenark.transactions.writer;

import com.mavenark.transactions.model.Employee;
import com.mavenark.transactions.repo.EmployeeRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@Slf4j
public class EmployeeDBWriter implements ItemWriter<Employee> {

    @Autowired
    private EmployeeRepo employeeRepo;

    @Override
    @Transactional
    public void write(List<? extends Employee> employees) throws Exception {
        if (employees == null || employees.isEmpty()) {
            log.info("No employees to write to the database.");
            return;
        }

        log.info("Saving {} employees to the database.", employees.size());

        employeeRepo.saveAll(employees);
        log.info("Saved {} employees to the database.", employees.size());
    }
}
