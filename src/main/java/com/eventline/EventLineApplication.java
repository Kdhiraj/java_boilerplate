package com.eventline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

@SpringBootApplication
@EnableMongoAuditing
@EnableMethodSecurity(securedEnabled = true)
@EnableAsync
public class EventLineApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventLineApplication.class, args);
	}

}
