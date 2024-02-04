package com.project.inhaUnsolved.scheduler.newproblemadd;

import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.scheduler.domain.LastUpdatedProblemNumber;
import com.project.inhaUnsolved.scheduler.domain.LastUpdatedProblemNumberRepository;
import com.project.inhaUnsolved.scheduler.dto.NewUnsolvedProblems;
import jakarta.persistence.EntityManagerFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemReaderAdapter;
import org.springframework.batch.item.adapter.ItemWriterAdapter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;

@Slf4j
@RequiredArgsConstructor
@Configuration
public class NewProblemAddJobConfig {

    public static final String JOB_NAME = "newProblemAddJob";
    private static final int chunkSize = 1;

    private final EntityManagerFactory emf;

    @Bean
    public Job newProblemAddJob(PlatformTransactionManager transactionManager, JobRepository jobRepository,
                                ProblemRequestByNumber request, LastUpdatedProblemNumberRepository numberRepository) {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(newProblemAddStep(transactionManager, jobRepository, request, numberRepository))
                .build();

    }

    @Bean
    @JobScope
    public Step newProblemAddStep(PlatformTransactionManager transactionManager, JobRepository repository,
                                  ProblemRequestByNumber request, LastUpdatedProblemNumberRepository numberRepository) {
        return new StepBuilder(JOB_NAME, repository)
                .<NewUnsolvedProblems, UnsolvedProblem>chunk(chunkSize, transactionManager)
                .reader(reader(request, numberRepository))
                .writer(writer())
                .build();

    }


    @Bean
    @StepScope
    public ItemReader<NewUnsolvedProblems> reader(ProblemRequestByNumber request,
                                                  LastUpdatedProblemNumberRepository numberRepository) {
        return new NewUnsolvedProblemsReader(numberRepository, request);

    }

    @Bean
    @StepScope
    public ItemWriter<UnsolvedProblem> writer() {
        ItemWriterAdapter<UnsolvedProblem> itemWriterAdapter = new ItemWriterAdapter<>();
        itemWriterAdapter.setTargetObject(NewProblemAddService.class);
        itemWriterAdapter.setTargetMethod("addProblems");
        return itemWriterAdapter;
    }



}
