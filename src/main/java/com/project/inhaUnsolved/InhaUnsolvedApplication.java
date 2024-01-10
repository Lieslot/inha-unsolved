package com.project.inhaUnsolved;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class InhaUnsolvedApplication {

    public static void main(String[] args) {
        SpringApplication.run(InhaUnsolvedApplication.class, args);
    }

}
