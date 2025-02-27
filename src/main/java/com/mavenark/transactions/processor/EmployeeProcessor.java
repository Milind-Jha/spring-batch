package com.mavenark.transactions.processor;

import com.mavenark.transactions.dto.EmployeeDTO;
import com.mavenark.transactions.document.Employee;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class EmployeeProcessor implements ItemProcessor<EmployeeDTO, Employee> {

    @Override
    public Employee process(EmployeeDTO employeeDTO) {
        if(employeeDTO==null)
            return null;
        Employee employee = new Employee();
        employee.setId(UUID.randomUUID().toString());
        employee.setFirstName(employeeDTO.getFirstName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setAge(employeeDTO.getAge());
        if (employee.getId() == null) {
            throw new IllegalArgumentException("Employee ID cannot be null");
        }
        if(employee.getFirstName()==null || employee.getLastName()==null)
            return null;
        log.info("Processed Employee: {}", employee);
        return employee;
    }
}
