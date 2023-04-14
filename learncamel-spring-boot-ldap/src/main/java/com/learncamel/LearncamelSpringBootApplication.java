package com.learncamel;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:bean.xml")
public class LearncamelSpringBootApplication {

	public static void main(String[] args) {
		SpringApplication.run(LearncamelSpringBootApplication.class, args);
	}
}
