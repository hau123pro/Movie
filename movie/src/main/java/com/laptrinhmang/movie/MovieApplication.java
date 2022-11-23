package com.laptrinhmang.movie;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

import com.laptrinhmang.movie.config.CustomSpringEventListener;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@ComponentScan({ "com.laptrinhmang.movie.config", "com.laptrinhmang.movie.service", "com.laptrinhmang.movie.filter",
		 "com.laptrinhmang.movie.dto", "com.laptrinhmang.movie.controller", "com.laptrinhmang.movie.utils" })
@EnableAutoConfiguration
public class MovieApplication {

	public static void main(String[] args) {
		SpringApplication springApplication=new SpringApplication(MovieApplication.class);
		SpringApplication.run(MovieApplication.class, args);
		
	}

}
