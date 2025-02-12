package com.student.app.bidworm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
@PropertySource("classpath:env.properties")
public class BidwormApplication {


	public static void main(String[] args) {

		SpringApplication.run(BidwormApplication.class, args);
	}

	}
