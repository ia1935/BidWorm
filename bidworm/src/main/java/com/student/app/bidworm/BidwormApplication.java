package com.student.app.bidworm;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@SpringBootApplication
public class BidwormApplication {


	public static void main(String[] args) {
		//valuable info loaded in. place in root directory
		Dotenv dotenv = Dotenv.load();


		SpringApplication.run(BidwormApplication.class, args);
	}

	}
