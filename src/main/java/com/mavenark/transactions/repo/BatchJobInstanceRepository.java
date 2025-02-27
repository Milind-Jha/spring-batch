package com.mavenark.transactions.repo;

import com.mavenark.transactions.entity.BatchJobInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchJobInstanceRepository extends JpaRepository<BatchJobInstance, Long> {
}
