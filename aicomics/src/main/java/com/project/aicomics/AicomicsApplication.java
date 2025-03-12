package com.project.aicomics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RestController;

@RestController
@SpringBootApplication
public class AicomicsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AicomicsApplication.class, args);
	}

}
