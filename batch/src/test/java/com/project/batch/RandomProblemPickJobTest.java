package com.project.batch;


import com.project.batch.randomproblem.RandomProblemPickJob;
import com.project.inhaUnsolved.domain.problem.domain.DailyRandomProblem;
import com.project.inhaUnsolved.domain.problem.domain.Problem;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.repository.DailyRandomProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashSet;
import java.util.List;
import java.util.stream.IntStream;

@ActiveProfiles("test")
public class RandomProblemPickJobTest extends BatchTestSupport {

    private static final int RANDOM_PROBLEM_COUNT = 10;


    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private DailyRandomProblemRepository dailyRandomProblemRepository;

    @Autowired
    private RandomProblemPickJob randomProblemPickJob;


    @BeforeEach
    void setUp() {

        List<Problem> problems = IntStream.range(1000, 1030)
                .mapToObj(number -> Problem.builder()
                        .number(number)
                        .tags(new HashSet<>())
                        .tier(Tier.BRONZE_IV)
                        .name(String.valueOf(number))
                        .build())
                .toList();
        problemRepository.saveAll(problems);


    }


    @Test
    void 정상_작동() throws Exception {

        launchJob(randomProblemPickJob.pickRandomProblem(), null);
        thenBatchCompleted();

        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);

        Assertions.assertThat(stepExecution.getWriteCount()).isEqualTo(RANDOM_PROBLEM_COUNT);

    }
    @Test
    void 이미_존재하는_상태에서_랜덤_문제가_잘_바뀌는지_테스트() throws Exception {

        List<DailyRandomProblem> prevRandomProblems = problemRepository.findRandomProblems(RANDOM_PROBLEM_COUNT)
                .stream()
                .map(randomProblem -> DailyRandomProblem.builder()
                        .id(null)
                        .problemId(randomProblem.getId())
                        .build())
                .toList();

        dailyRandomProblemRepository.saveAll(prevRandomProblems);

        launchJob(randomProblemPickJob.pickRandomProblem(), null);
        thenBatchCompleted();

        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);
        Assertions.assertThat(stepExecution.getWriteCount()).isEqualTo(RANDOM_PROBLEM_COUNT);

        List<DailyRandomProblem> newRandomProblems = dailyRandomProblemRepository.findAll();

        List<Integer> prevProblemIds = prevRandomProblems.stream()
                .map(DailyRandomProblem::getProblemId)
                .toList();

        List<Integer> newProblemIds = newRandomProblems.stream()
                .map(DailyRandomProblem::getProblemId)
                .toList();

        Assertions.assertThat(prevProblemIds)
                .isNotEqualTo(newProblemIds);
    }

        @AfterEach
    void tearDown() throws Exception {
        dailyRandomProblemRepository.deleteAll();
        problemRepository.deleteAll();
    }

}

