package com.stranger.blogify;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BackendApplication {

	public static void main(String[] args) {
		if (System.getenv("RENDER") == null) { // null means not on Render → local
			Dotenv dotenv = Dotenv.load();
			dotenv.entries().forEach(entry ->
				System.setProperty(entry.getKey(), entry.getValue())
			);
		}
		SpringApplication.run(BackendApplication.class, args);
	}

}
