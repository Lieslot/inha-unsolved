package com.project.inhaUnsolved.problem.batch.config;


import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.config.QuerydslConfig;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import java.util.Queue;
import org.hibernate.annotations.Immutable;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;


@Configuration
@EnableAutoConfiguration
@Import({QuerydslConfig.class, ProblemRequestByNumber.class})
public class TestBatchConfig {

}
