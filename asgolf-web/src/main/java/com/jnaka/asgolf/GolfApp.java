package com.jnaka.asgolf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
@ImportResource("classpath:applicationContext.xml")
@ComponentScan(basePackages = { "com.jnaka.asgolf", "com.jnaka.golf" })
public class GolfApp {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(GolfApp.class, args);
	}
	
	
}