package com.paulocezarjr.contasapagar;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.paulocezarjr.contasapagar")
public class Application {
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}
}
