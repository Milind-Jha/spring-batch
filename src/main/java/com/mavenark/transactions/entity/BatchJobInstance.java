package com.mavenark.transactions.entity;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class BatchJobInstance {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobInstanceId;
    private String jobName;
}
