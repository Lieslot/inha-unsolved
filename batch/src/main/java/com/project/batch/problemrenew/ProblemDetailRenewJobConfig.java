package com.project.batch.problemrenew;


import com.project.api.ProblemRequestByNumber;
import com.project.batch.dto.ProblemIdNumber;
import com.project.inhaUnsolved.domain.problem.domain.QUnsolvedProblem;
import com.project.reader.QuerydslNoOffsetPagingItemReader;
import com.project.reader.expression.Expression;
import com.project.reader.options.QuerydslNoOffsetNumberOptions;
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
import org.springframework.batch.core.step.tasklet.TaskletStep;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class ProblemDetailRenewJobConfig {
    public static final String JOB_NAME = "problemDetailRenewJob";
    private static final int chunkSize = 100;

    private final EntityManagerFactory emf;
    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final ProblemRequestByNumber request;
    private final ProblemDetailRenewService service;

    @Bean
    public Job problemDetailRenewJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(problemDetailRenewStep())
                .build();
    }

    @Bean
    @JobScope
    public Step problemDetailRenewStep() {
        return new StepBuilder("problemDetailRenewStep", jobRepository)
                .<ProblemIdNumber, ProblemIdNumber>chunk(chunkSize, transactionManager)
                .reader(problemDetailRenewReader())
                .writer(problemDetailRenewWriter())
                .build();

    }

    @Bean
    @StepScope
    public QuerydslNoOffsetPagingItemReader<ProblemIdNumber> problemDetailRenewReader() {
        QuerydslNoOffsetNumberOptions<ProblemIdNumber, Integer> options =
                new QuerydslNoOffsetNumberOptions<>(QUnsolvedProblem.unsolvedProblem.id, Expression.ASC);

        return new QuerydslNoOffsetPagingItemReader<>(emf, chunkSize, options, queryFactory -> queryFactory
                .select(Projections.constructor(ProblemIdNumber.class, QUnsolvedProblem.unsolvedProblem.id,
                        QUnsolvedProblem.unsolvedProblem.number))
                .from(QUnsolvedProblem.unsolvedProblem)
                .setLockMode(LockModeType.PESSIMISTIC_WRITE));
    }

    @Bean
    @StepScope
    public ItemWriter<ProblemIdNumber> problemDetailRenewWriter() {
        return new ProblemDetailRenewWriter(request, service);
    }
}
