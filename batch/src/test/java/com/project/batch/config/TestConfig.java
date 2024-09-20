package com.project.batch.config;


import com.project.inhaUnsolved.domain.problem.domain.Problem;
import com.project.reader.QuerydslNoOffsetPagingItemReader;
import com.project.reader.expression.Expression;
import com.project.reader.options.QuerydslNoOffsetNumberOptions;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

import static com.project.inhaUnsolved.domain.problem.domain.QProblem.problem;

@TestConfiguration
@RequiredArgsConstructor
public class TestConfig {

    private final EntityManagerFactory emf;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    public static final String JOB_NAME = "testJob";
    private static final int chunkSize = 100;



    @Bean
    public Job testJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(testStep())
                .build();
    }

    @Bean
    @JobScope
    public Step testStep() {
        return new StepBuilder("testStep", jobRepository)
                .<Problem, Problem>chunk(chunkSize, transactionManager)
                .reader(testReader())
                .processor(testProcessor())
                .writer(testWriter())
                .transactionAttribute(new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_NOT_SUPPORTED))
                .build();

    }

    @Bean
    @StepScope
    public QuerydslNoOffsetPagingItemReader<Problem> testReader() {
        QuerydslNoOffsetNumberOptions<Problem, Integer> options =
                new QuerydslNoOffsetNumberOptions<>(problem.id, Expression.ASC);

        return new QuerydslNoOffsetPagingItemReader<>(emf, chunkSize, options, queryFactory -> queryFactory.
                selectFrom(problem)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE));
    }

    @Bean
    @StepScope
    public ItemProcessor<Problem, Problem> testProcessor() {
        return item -> {
            item.renewName("test");
            return item;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<Problem> testWriter() {
        JpaItemWriter<Problem> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(emf);
        return itemWriter;
    }
}
