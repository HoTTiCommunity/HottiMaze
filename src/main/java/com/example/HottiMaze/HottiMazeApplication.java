package com.example.HottiMaze;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class HottiMazeApplication {
	public static void main(String[] args) {
		SpringApplication.run(HottiMazeApplication.class, args);
	}
}
