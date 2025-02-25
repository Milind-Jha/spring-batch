package com.mavenark.transactions.repo;


import com.mavenark.transactions.entity.BatchJobInstance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchMetaDataRepo extends JpaRepository<BatchJobInstance, Long> {

}
