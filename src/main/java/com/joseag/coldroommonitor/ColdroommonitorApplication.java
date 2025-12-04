package com.joseag.coldroommonitor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ColdroommonitorApplication {

	public static void main(String[] args) {
		SpringApplication.run(ColdroommonitorApplication.class, args);
	}

}
