package com.lyarmovs;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * Main SpringBootApplication class {@link SpringBootApplication @SpringBootApplication}
 *
 * The app can be compiled by maven
 *
 * mvn clean install
 *
 * Can be started in command line
 *
 * java -jar target\spring-file-manager-0.0.1-SNAPSHOT.jar
 */
@SpringBootApplication
@EnableScheduling
public class SpringFileManagerApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringFileManagerApplication.class, args);
	}
}
