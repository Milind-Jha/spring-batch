package com.mavenark.transactions.repo;


import com.mavenark.transactions.entity.BatchStepExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchStepExecutionRepository extends JpaRepository<BatchStepExecution, Long> {
}
