package com.backendfunda.backendfunda;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BackendfundaApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendfundaApplication.class, args);
	}

}
