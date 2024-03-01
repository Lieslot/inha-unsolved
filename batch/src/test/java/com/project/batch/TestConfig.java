package com.project.batch;


import com.project.api.ExternalApiApplication;
import com.project.inhaUnsolved.InhaUnsolvedApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {InhaUnsolvedApplication.class, ExternalApiApplication.class})
public class TestConfig {
}
