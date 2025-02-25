package com.mavenark.transactions.processor;

import com.mavenark.transactions.dto.EmployeeDTO;
import com.mavenark.transactions.model.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class EmployeeProcessor implements ItemProcessor<EmployeeDTO, Employee> {

    @Override
    public Employee process(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        employee.setEmployeeId(UUID.randomUUID().toString());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setAge(employeeDTO.getAge());
        if (employee.getEmployeeId() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        log.info("Processed Employee: {}", employee);
        return employee;
    }
}
