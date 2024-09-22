package com.project.batch.randomproblem;

import com.project.inhaUnsolved.domain.problem.domain.DailyRandomProblem;
import com.project.inhaUnsolved.domain.problem.repository.DailyRandomProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.repeat.RepeatStatus;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RandomProblemPickJob {

    private static final String JOB_NAME = "randomProblemPickJob";
    private static final String STEP_NAME = "randomProblemPickStep";
    private static final int RANDOM_PROBLEM_COUNT = 10;

    private final PlatformTransactionManager transactionManager;
    private final JobRepository jobRepository;
    private final ProblemRepository problemRepository;
    private final DailyRandomProblemRepository repository;


    @Bean
    public Job pickRandomProblem() {
        return new JobBuilder(JOB_NAME, jobRepository)
                .start(pickRandomProblemStep())
                .build();
    }

    @Bean
    @JobScope
    public Step pickRandomProblemStep() {
        return new StepBuilder(STEP_NAME, jobRepository)
                .tasklet(pickRandomProblemTasklet(), transactionManager)
                .build();
    }
    @StepScope
    @Bean
    public Tasklet pickRandomProblemTasklet() {
        return (StepContribution contribution, ChunkContext chunkContext) -> {
            repository.deleteAll();
            List<DailyRandomProblem> randomProblems = selectRandomProblems();
            repository.saveAll(randomProblems);

            contribution.incrementWriteCount(randomProblems.size());
            return RepeatStatus.FINISHED;
        };
    }


    public List<DailyRandomProblem> selectRandomProblems() {
        return problemRepository.findRandomProblems(RANDOM_PROBLEM_COUNT)
                .stream()
                .map(randomProblem -> DailyRandomProblem.builder()
                        .id(null)
                        .problemId(randomProblem.getId())
                        .build())
                .toList();
    }


}
