package com.project.batch;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BatchApplication {
    public static void main(String[] args) {
        final var context = SpringApplication.run(BatchApplication.class, args);
        System.exit(SpringApplication.exit(context));
    }
}