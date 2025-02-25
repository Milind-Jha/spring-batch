package com.mavenark.transactions.repo;

import com.mavenark.transactions.document.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends MongoRepository<Employee, Integer> {
}
