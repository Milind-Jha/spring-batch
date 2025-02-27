package com.mavenark.transactions.entity;

import lombok.Data;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "BATCH_STEP_EXECUTION")
@Data
public class BatchStepExecution {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long stepExecutionId;

    @ManyToOne
    @JoinColumn(name = "job_execution_id")
    private BatchJobExecution jobExecution;

    private String stepName;
    private Date startTime;
    private Date endTime;
    private String status;
}
