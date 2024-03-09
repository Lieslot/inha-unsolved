package com.project.batch.newproblemadd;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.dto.NewUnsolvedProblems;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.LastUpdatedProblemNumberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class NewProblemAddJobConfig {

    public static final String JOB_NAME = "newProblemAddJob";
    private static final int chunkSize = 1;

    private final LastUpdatedProblemNumberRepository numberRepository;

    private final NewProblemAddService newProblemAddService;

    private final ProblemRequestByNumber request;

    private final PlatformTransactionManager transactionManager;

    private final JobRepository jobRepository;

    @Bean
    public Job newProblemAddJob() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(newProblemAddStep())
                .build();

    }

    @Bean
    @JobScope
    public Step newProblemAddStep() {
        return new StepBuilder("newProblemAddStep", jobRepository)
                .<NewUnsolvedProblems, UnsolvedProblem>chunk(chunkSize, transactionManager)
                .reader(newUnsolvedProblemsReader())
                .writer(newUnsolvedProblemsWriter())
                .build();

    }


    @Bean
    @StepScope
    public ItemStreamReader<NewUnsolvedProblems> newUnsolvedProblemsReader() {
        return new NewUnsolvedProblemsReader(numberRepository, request);

    }

    @Bean
    @StepScope
    public ItemWriterAdapter<UnsolvedProblem> newUnsolvedProblemsWriter() {

        ItemWriterAdapter<UnsolvedProblem> itemWriterAdapter = new ItemWriterAdapter<>();
        itemWriterAdapter.setTargetObject(newProblemAddService);
        itemWriterAdapter.setTargetMethod("addProblems");
        return itemWriterAdapter;
    }


}
