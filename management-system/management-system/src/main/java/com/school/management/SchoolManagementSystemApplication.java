package com.school.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;

import java.util.Optional;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@SpringBootApplication
@EnableAsync
public class SchoolManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(SchoolManagementSystemApplication.class, args);
	}
    @Bean
    public AuditorAware<String> auditorProvider() {
        return () -> Optional.ofNullable("SchoolAdmin");
    }
}
