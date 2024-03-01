package com.project.batch.deletecheck;

import com.project.batch.dto.ProblemAndUser;
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
public class ProblemDeleteCheckJob {

    private static final String JOB_NAME = "problemDeleteCheckJob";

    private final PlatformTransactionManager transactionManager;
    private final NewSolvedProblemService newSolvedProblemService;
    private final JobRepository jobRepository;


    public Job problemDeleteCheckJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(problemDeleteCheckStep())
                .build();
    }

    @Bean
    @JobScope
    public Step problemDeleteCheckStep() {
        return new StepBuilder("problemDeleteCheckStep", jobRepository)
                .<ProblemAndUser, ProblemAndUser>chunk(1, transactionManager)
                .reader(newSolvedProblemReader())
                .writer(newSolvedProblemWriter())
                .build();
    }

    @Bean
    @StepScope
    public NewSolvedProblemReader newSolvedProblemReader() {
        return new NewSolvedProblemReader(newSolvedProblemService);
    }

    @Bean
    @StepScope
    public NewSolvedProblemWriter newSolvedProblemWriter() {
        return new NewSolvedProblemWriter(newSolvedProblemService);
    }


}
