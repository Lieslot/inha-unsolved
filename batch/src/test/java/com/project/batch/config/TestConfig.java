package com.project.batch.config;


import com.project.inhaUnsolved.domain.problem.domain.QUnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
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
                .<UnsolvedProblem, UnsolvedProblem>chunk(chunkSize, transactionManager)
                .reader(testReader())
                .processor(testProcessor())
                .writer(testWriter())
                .transactionAttribute(new DefaultTransactionAttribute(TransactionDefinition.PROPAGATION_NOT_SUPPORTED))
                .build();

    }

    @Bean
    @StepScope
    public QuerydslNoOffsetPagingItemReader<UnsolvedProblem> testReader() {
        QuerydslNoOffsetNumberOptions<UnsolvedProblem, Integer> options =
                new QuerydslNoOffsetNumberOptions<>(QUnsolvedProblem.unsolvedProblem.id, Expression.ASC);

        return new QuerydslNoOffsetPagingItemReader<>(emf, chunkSize, options, queryFactory -> queryFactory.
                selectFrom(QUnsolvedProblem.unsolvedProblem)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE));
    }

    @Bean
    @StepScope
    public ItemProcessor<UnsolvedProblem, UnsolvedProblem> testProcessor() {
        return item -> {
            item.renewName("test");
            return item;
        };
    }

    @Bean
    @StepScope
    public ItemWriter<UnsolvedProblem> testWriter() {
        JpaItemWriter<UnsolvedProblem> itemWriter = new JpaItemWriter<>();
        itemWriter.setEntityManagerFactory(emf);
        return itemWriter;
    }
}
