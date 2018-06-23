package com.xbb.spring_jfinal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class TruboApplication {

	public static void main(String[] args) {
		SpringApplication.run(TruboApplication.class, args);
	}
	
}
