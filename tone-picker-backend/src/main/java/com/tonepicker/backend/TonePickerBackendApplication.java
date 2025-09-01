package com.tonepicker.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// This annotation marks the class as a Spring Boot application.
// It combines @Configuration, @EnableAutoConfiguration, and @ComponentScan.
@SpringBootApplication
public class TonePickerBackendApplication {

    // The main method is the entry point of the Spring Boot application.
    // It calls the run() method to start the application context.
	public static void main(String[] args) {
		SpringApplication.run(TonePickerBackendApplication.class, args);
	}

}