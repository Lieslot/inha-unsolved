package com.project.batch;

import com.project.api.ProblemRequestByNumber;
import com.project.batch.newproblemadd.NewUnsolvedProblemAddJobConfig;
import com.project.inhaUnsolved.domain.problem.domain.LastUpdatedProblemNumber;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;


public class NewProblemAddJobTest extends BatchTestSupport {


    @Autowired
    private NewUnsolvedProblemAddJobConfig newProblemAddJobConfig;

    @MockBean
    private ProblemRequestByNumber request;
    @Autowired
    private ProblemRepository problemRepository;


    @Test
    void 실행_테스트() throws Exception {
        List<String> problemNumbers = new ArrayList<>();
        List<UnsolvedProblem> problems = new ArrayList<>();

        for (int i = 1000; i <= 1106; i++) {
            UnsolvedProblem test = UnsolvedProblem.builder()
                                                  .number(i)
                                                  .tags(new HashSet<>())
                                                  .tier(Tier.BRONZE_IV)
                                                  .name(String.format("test %d", i))
                                                  .build();
            if (i <= 1006){
                save(test);
            }

            else {
                problemNumbers.add(String.valueOf(i));
                problems.add(test);
            }

        }

        save(new LastUpdatedProblemNumber(1006));

        Mockito.when(request.getProblemBy(problemNumbers)).thenReturn(problems);

        launchJob(newProblemAddJobConfig.newProblemAddJob(), null);


        List<UnsolvedProblem> problems1 = problemRepository.findAll();
        Assertions.assertThat(problems1.size()).isEqualTo(107);

        Assertions.assertThat(problems1.size()).isEqualTo(107);

    }

}
