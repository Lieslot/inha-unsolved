package com.project.inhaUnsolved.scheduler.problemrenew;

import static com.project.inhaUnsolved.domain.problem.domain.QUnsolvedProblem.unsolvedProblem;

import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.dto.ProblemMinDetail;
import com.querydsl.core.types.Projections;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.LockModeType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import reader.QuerydslNoOffsetPagingItemReader;
import reader.expression.Expression;
import reader.options.QuerydslNoOffsetNumberOptions;


@Slf4j
@RequiredArgsConstructor
@Configuration
public class ProblemDetailRenewJobConfig {
    public static final String JOB_NAME = "problemDetailRenewJob";
    private static final int chunkSize = 100;

    private final EntityManagerFactory emf;


    @Bean
    public Job problemDetailRenewJob(PlatformTransactionManager transactionManager, JobRepository jobRepository,
                                     ProblemRequestByNumber request, ProblemDetailRenewService service) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(problemDetailRenewStep(transactionManager, jobRepository, request, service))
                .build();
    }

    @Bean
    @JobScope
    public Step problemDetailRenewStep(PlatformTransactionManager transactionManager, JobRepository jobRepository,
                                       ProblemRequestByNumber request, ProblemDetailRenewService service) {
        return new StepBuilder("problemDetailRenewStep", jobRepository)
                .<ProblemMinDetail, ProblemMinDetail>chunk(chunkSize, transactionManager)
                .reader(reader())
                .writer(writer(request, service))
                .transactionAttribute(
                        new DefaultTransactionAttribute(DefaultTransactionDefinition.PROPAGATION_NOT_SUPPORTED))
                .build();
    }

    @Bean
    @StepScope
    public QuerydslNoOffsetPagingItemReader<ProblemMinDetail> reader() {
        // 1. No Offset 옵션
        QuerydslNoOffsetNumberOptions <ProblemMinDetail, Integer> options =
                new QuerydslNoOffsetNumberOptions<>(unsolvedProblem.id, Expression.ASC);

        return new QuerydslNoOffsetPagingItemReader<>(emf, chunkSize, options, queryFactory -> queryFactory
                .select(Projections.constructor(ProblemMinDetail.class, unsolvedProblem.id, unsolvedProblem.number))
                .from(unsolvedProblem)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE));

    }

    @Bean
    @StepScope
    public ItemWriter<ProblemMinDetail> writer(ProblemRequestByNumber request, ProblemDetailRenewService service) {
        return new ProblemDetailRenewWriter(request, emf, service);
    }
}
