package com.project.inhaUnsolved.scheduler.tag;


import static com.project.inhaUnsolved.domain.problem.domain.QTag.tag;

import com.project.inhaUnsolved.domain.problem.api.TagRequest;
import com.project.inhaUnsolved.domain.problem.domain.Tag;
import com.project.inhaUnsolved.domain.problem.service.TagService;
import jakarta.persistence.EntityManagerFactory;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.interceptor.DefaultTransactionAttribute;
import reader.QuerydslNoOffsetPagingItemReader;
import reader.expression.Expression;
import reader.options.QuerydslNoOffsetNumberOptions;

@Configuration
@RequiredArgsConstructor
public class TagDetailRenewJobConfig {


    public static final String JOB_NAME = "tagDetailRenewJobConfig";

    private final TagService tagService;
    private final TagRequest tagRequest;
    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;

    @Bean
    public Job tagDetailRenewJob() {

        return new JobBuilder(JOB_NAME, jobRepository)
                .start(tagDetailRenewStep())
                .build();

    }

    @Bean
    @JobScope
    public Step tagDetailRenewStep() {
        return new StepBuilder("tagDetailRenewStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    List<Tag> tagDetails = tagRequest.getTagDetails();
                    tagService.saveAll(tagDetails);
                    return RepeatStatus.FINISHED;
                }) , transactionManager)
                .build();
    }



}
