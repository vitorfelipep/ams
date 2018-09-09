package com.ams.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import com.ams.api.config.properties.AmsProperties;

@SpringBootApplication
@EnableConfigurationProperties(AmsProperties.class)
public class AmsApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(AmsApiApplication.class, args);
	}
}
