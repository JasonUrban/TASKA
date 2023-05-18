package com.example.taska;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TaskaApplication {
	public static void main(String[] args) {
		SpringApplication.run(TaskaApplication.class, args);
	}
}
