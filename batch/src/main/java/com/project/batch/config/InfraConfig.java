package com.project.batch.config;


import com.project.api.ExternalApiApplication;
import com.project.inhaUnsolved.config.JpaConfig;
import com.project.inhaUnsolved.service.DomainServicePackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = {DomainServicePackage.class, ExternalApiApplication.class})
@Import(JpaConfig.class)
public class InfraConfig {
}
