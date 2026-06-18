package com.gym.tracker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GymTrackerApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymTrackerApplication.class, args);
        System.out.println("\n========================================");
        System.out.println("  Gym Tracker API is running!");
        System.out.println("  Base URL : http://localhost:8080/api");
        System.out.println("  MongoDB  : gym_tracker_db");
        System.out.println("========================================\n");
    }
}
