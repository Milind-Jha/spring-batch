package com.mavenark.transactions.repo;

import com.mavenark.transactions.entity.BatchJobExecution;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobExecutionRepository extends JpaRepository<BatchJobExecution, Long> {
}