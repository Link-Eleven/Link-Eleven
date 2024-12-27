package com.linkeleven.msa.area;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AreaApplication {

	public static void main(String[] args) {
		SpringApplication.run(AreaApplication.class, args);
	}

}
