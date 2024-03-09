package com.project.internal.config;


import com.project.inhaUnsolved.config.JpaConfig;
import com.project.inhaUnsolved.service.DomainServicePackage;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ComponentScan(basePackageClasses = {DomainServicePackage.class})
@Import(JpaConfig.class)
public class InfraConfig {
}
