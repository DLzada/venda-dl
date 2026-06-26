package com.daniel.loja_dl_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class LojaDlApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(LojaDlApiApplication.class, args);
	}

}
