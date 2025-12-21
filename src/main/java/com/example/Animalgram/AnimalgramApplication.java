package com.example.Animalgram;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class AnimalgramApplication {

	public static void main(String[] args) {
		SpringApplication.run(AnimalgramApplication.class, args);
	}

}
