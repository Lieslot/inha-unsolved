package com.project.inhaUnsolved.scheduler.problemrenew;

import static com.project.inhaUnsolved.domain.problem.domain.QUnsolvedProblem.unsolvedProblem;

import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import reader.QuerydslNoOffsetPagingItemReader;
import reader.expression.Expression;
import reader.options.QuerydslNoOffsetNumberOptions;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class ProblemDetailRenewJobConfig {
    public static final String JOB_NAME = "problemDetailRenewJob";
    private static final int chunkSize = 50;

    private final EntityManagerFactory emf;


    @Bean
    public Job problemDetailRenewJob(PlatformTransactionManager transactionManager, JobRepository jobRepository,
                                     ProblemRequestByNumber request) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(problemDetailRenewStep(transactionManager, jobRepository, request))
                .build();
    }

    @Bean
    public Step problemDetailRenewStep(PlatformTransactionManager transactionManager, JobRepository jobRepository,
                                       ProblemRequestByNumber request) {
        return new StepBuilder("problemDetailRenewStep", jobRepository)
                .<UnsolvedProblem, UnsolvedProblem>chunk(chunkSize, transactionManager)
                .reader(reader())
                .writer(writer(request))
                .build();
    }

    @Bean
    public QuerydslNoOffsetPagingItemReader<UnsolvedProblem> reader() {
        // 1. No Offset 옵션
        QuerydslNoOffsetNumberOptions<UnsolvedProblem, Integer> options =
                new QuerydslNoOffsetNumberOptions<>(unsolvedProblem.id, Expression.ASC);

        return new QuerydslNoOffsetPagingItemReader<>(emf, chunkSize, options,  queryFactory -> queryFactory
                .selectFrom(unsolvedProblem)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE));

    }

    @Bean
    public ItemWriter<UnsolvedProblem> writer(ProblemRequestByNumber request) {
        return new ProblemDetailRenewWriter(request, emf);
    }
}
