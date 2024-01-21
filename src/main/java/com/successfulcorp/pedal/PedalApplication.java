package com.successfulcorp.pedal;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class PedalApplication {

	public static void main(String[] args) {
		log.info("Starting Application");
		SpringApplication.run(PedalApplication.class, args);
		log.info("Application started successfully");
	}

	@PreDestroy
	public void onExit() {
		log.info("Application is shutting down");
	}

}
