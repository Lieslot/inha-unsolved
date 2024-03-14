package com.project.batch.solvecheck;

import com.project.batch.dto.ProblemAndUser;
import com.project.inhaUnsolved.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;


@Configuration
@RequiredArgsConstructor
public class ProblemSolveCheckJobConfig {

    private static final String JOB_NAME = "problemSolveCheckJob";
    private static final int chunkSize= 10;

    private final PlatformTransactionManager transactionManager;
    private final NewSolvedProblemService newSolvedProblemService;
    private final JobRepository jobRepository;


    @Bean
    public Job problemSolveCheckJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(problemDeleteCheckStep())
                .build();
    }

    @Bean
    @JobScope
    public Step problemDeleteCheckStep() {
        return new StepBuilder("problemDeleteCheckStep", jobRepository)
                .<User, User>chunk(chunkSize, transactionManager)
                .reader(renewedUserPagingItemReader())
                .writer(newSolvedProblemWriter())
                .build();
    }

    @Bean
    @StepScope
    public RenewedUserPagingItemReader renewedUserPagingItemReader() {
        return new RenewedUserPagingItemReader(chunkSize, newSolvedProblemService);
    }

    @Bean
    @StepScope
    public NewSolvedProblemWriter newSolvedProblemWriter() {
        return new NewSolvedProblemWriter(newSolvedProblemService);
    }


}
