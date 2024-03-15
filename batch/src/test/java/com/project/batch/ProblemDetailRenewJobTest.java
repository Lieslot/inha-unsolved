package com.project.batch;


import static org.mockito.Mockito.when;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.dto.ProblemIdNumber;
import com.project.batch.problemrenew.ProblemDetailRenewJobConfig;
import com.project.batch.problemrenew.ProblemDetailRenewService;
import com.project.batch.problemrenew.ProblemDetailRenewWriter;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.item.Chunk;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;


@ActiveProfiles("test")
public class ProblemDetailRenewJobTest extends BatchTestSupport {

    @MockBean
    private ProblemRequestByNumber request;
    @Autowired
    private ProblemDetailRenewService service;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private ProblemDetailRenewJobConfig jobConfig;


    @AfterEach
    void delete() {
        problemRepository.deleteAll();
    }

    @Test
    void 실행_테스트() throws Exception {
        List<String> problemNumbers = new ArrayList<>();
        List<UnsolvedProblem> problems = new ArrayList<>();
        for (int i = 1000; i <= 1099; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name(String.format("test %d", i))
                                                  .build();
            UnsolvedProblem changed = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name("test")
                                                  .build();
            problemRepository.save(test);

            problems.add(changed);
            problemNumbers.add(String.valueOf(i));

        }

        when(request.getProblemBy(problemNumbers)).thenReturn(problems);

        launchJob(jobConfig.problemDetailRenewJob(), null);

        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);

        List<UnsolvedProblem> changedProblems = problemRepository.findAll();

        changedProblems.forEach(problem -> {
            Assertions.assertThat(problem.getName()).isEqualTo("test");

        });


       Assertions.assertThat(stepExecution.getCommitCount());


    }


    @Test
    void 쓰기_테스트() throws Exception {

        ProblemDetailRenewWriter renewWriter = new ProblemDetailRenewWriter(request, service);
        List<ProblemIdNumber> problemMinDetails = new ArrayList<>();
        for (int i = 1000; i <= 1011; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name(String.format("test %d", i))
                                                  .build();
            problemRepository.save(test);
            problemMinDetails.add(new ProblemIdNumber(
                    test.getId()
                    , test.getNumber()));

        }

        List<UnsolvedProblem> newProblemDetail = new ArrayList<>();
        List<String> problemNumbers = new ArrayList<>();
        for (int i = 1000; i <= 1011; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name("test")
                                                  .build();

            newProblemDetail.add(test);
            problemNumbers.add(String.valueOf(i));
        }

        when(request.getProblemBy(problemNumbers)).thenReturn(newProblemDetail);

        renewWriter.write(new Chunk<>(problemMinDetails));

        List<UnsolvedProblem> result = problemRepository.findByNumberIn(problemNumbers.stream().
                                                                                      map(Integer::parseInt)
                .collect(Collectors.toList()));

        result.forEach(problem -> {
            Assertions.assertThat(problem.getName())
                      .isEqualTo("test");
        });

    }




}



