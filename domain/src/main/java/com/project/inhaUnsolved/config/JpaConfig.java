package com.project.inhaUnsolved.config;


import com.project.inhaUnsolved.DomainPackageLocation;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;


//
@Configuration
@EnableJpaAuditing
@EntityScan(basePackageClasses = DomainPackageLocation.class)
@EnableJpaRepositories(basePackageClasses = DomainPackageLocation.class)
@ComponentScan(basePackageClasses = {DomainPackageLocation.class}, //repositoryCustomIml 스캔
        includeFilters = @Filter(type = FilterType.ANNOTATION, classes = Repository.class))
public class JpaConfig {
}



