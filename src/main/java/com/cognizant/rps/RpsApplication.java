package com.cognizant.rps;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class RpsApplication {

	public static void main(String[] args) {
		SpringApplication.run(RpsApplication.class, args);
	}

}
