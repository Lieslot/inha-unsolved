package com.project.batch.config;


import com.project.api.ExternalApiApplication;
import com.project.inhaUnsolved.DomainPackageLocation;
import com.project.inhaUnsolved.config.JpaConfig;
import com.project.inhaUnsolved.service.DomainServicePackage;
import jakarta.persistence.Entity;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;

@Configuration
@ComponentScan(basePackageClasses = {DomainServicePackage.class, ExternalApiApplication.class})
@Import(JpaConfig.class)
public class InfraConfig {
}
