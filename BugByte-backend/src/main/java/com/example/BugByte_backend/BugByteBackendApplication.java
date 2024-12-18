package com.example.BugByte_backend;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

@SpringBootApplication
@EnableElasticsearchRepositories(basePackages = "com.example.BugByte_backend.repositories")
public class BugByteBackendApplication {

	public static void main(String[] args) {

		SpringApplication.run(BugByteBackendApplication.class, args);
	}

}
