package com.mavenark.transactions.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BATCH_JOB_EXECUTION")
@Data
public class BatchJobExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobExecutionId;

    @ManyToOne
    @JoinColumn(name = "job_instance_id")
    private BatchJobInstance jobInstance;

    private Date startTime;
    private Date endTime;
    private String status;
}
