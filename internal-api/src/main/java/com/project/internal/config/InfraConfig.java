package com.project.internal.config;


import com.project.inhaUnsolved.config.JpaConfig;
import com.project.inhaUnsolved.service.DomainServicePackage;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScan.Filter;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Configuration
@ComponentScan(basePackageClasses = {DomainServicePackage.class})
@Import(JpaConfig.class)
public class InfraConfig {
}
