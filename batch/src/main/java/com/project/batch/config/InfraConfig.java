package com.project.batch.config;


import com.project.api.ExternalApiApplication;
import com.project.inhaUnsolved.DomainPackageLocation;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = {DomainPackageLocation.class, ExternalApiApplication.class})
public class InfraConfig {
}
