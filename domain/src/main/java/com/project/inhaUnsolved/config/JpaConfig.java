package com.project.inhaUnsolved.config;



import com.project.inhaUnsolved.DomainPackageLocation;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaAuditing
@EntityScan(basePackageClasses = DomainPackageLocation.class)
@EnableJpaRepositories(basePackageClasses = DomainPackageLocation.class)
public class JpaConfig {}
