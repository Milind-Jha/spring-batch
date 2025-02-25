package com.mavenark.transactions;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan
public class SpringBatchDemo1Application {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchDemo1Application.class, args);
	}

}
