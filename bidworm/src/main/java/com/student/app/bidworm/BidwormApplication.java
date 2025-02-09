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

		loadAndPrintEnvProperties();




		SpringApplication.run(BidwormApplication.class, args);
	}

	private static void loadAndPrintEnvProperties() {
		Properties properties = new Properties();

		try (InputStream input = BidwormApplication.class.getClassLoader().getResourceAsStream("env.properties")) {
			if (input == null) {
				System.err.println("Error: env.properties file not found in src/main/resources");
				return;
			}
			// Load the properties
			properties.load(input);

			// Print all properties to the console
			properties.forEach((key, value) -> System.out.println(key + "=" + value));
		} catch (IOException e) {
			System.err.println("Error reading env.properties file: " + e.getMessage());
		}
	}



}
