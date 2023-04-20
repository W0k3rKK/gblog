package com.gblog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class GblogApplication {

	public static void main(String[] args) {
		SpringApplication.run(GblogApplication.class, args);
	}

}
