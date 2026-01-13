package com.example.commercetoolsDemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class CommercetoolsDemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(CommercetoolsDemoApplication.class, args);
	}

}
