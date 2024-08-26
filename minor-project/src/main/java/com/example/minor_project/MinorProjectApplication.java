package com.example.minor_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MinorProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinorProjectApplication.class, args);
	}

}
