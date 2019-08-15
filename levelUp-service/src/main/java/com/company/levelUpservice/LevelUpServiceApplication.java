package com.company.levelUpservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class LevelUpServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(LevelUpServiceApplication.class, args);
	}

}
