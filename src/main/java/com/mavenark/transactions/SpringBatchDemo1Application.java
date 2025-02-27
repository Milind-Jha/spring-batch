package com.mavenark.transactions;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import javax.sql.DataSource;

@SpringBootApplication
@EnableBatchProcessing
@ComponentScan
public class SpringBatchDemo1Application implements CommandLineRunner {

	@Autowired
	private DataSource dataSource;

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchDemo1Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("DataSource class: " + dataSource.getClass().getName());
	}
}
