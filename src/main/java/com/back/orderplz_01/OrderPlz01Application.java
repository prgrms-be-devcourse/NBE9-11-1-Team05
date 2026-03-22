package com.back.orderplz_01;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class OrderPlz01Application {

	public static void main(String[] args) {
		SpringApplication.run(OrderPlz01Application.class, args);
	}

}
