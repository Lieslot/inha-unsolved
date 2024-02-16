package com.project.inhaUnsolved.problem.batch;

import static org.mockito.Mockito.when;

import com.project.inhaUnsolved.domain.problem.api.ProblemRequestSolvedByUser;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepository;
import com.project.inhaUnsolved.domain.user.User;
import com.project.inhaUnsolved.domain.user.api.UserDetailRequest;
import com.project.inhaUnsolved.scheduler.deletecheck.ProblemDeleteCheckJob;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;


@ActiveProfiles("test")
public class ProblemDeleteCheckJobTest extends BatchTestSupport {

    @Autowired
    private ProblemDeleteCheckJob job;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private SolvedProblemRepository solvedProblemRepository;
    @MockBean
    private ProblemRequestSolvedByUser problemRequestSolvedByUser;
    @MockBean
    private UserDetailRequest userDetailRequest;


    @Test
    void 동작_테스트() throws Exception {

        List<UnsolvedProblem> savedProblems = IntStream.range(1000, 1200)
                                                       .mapToObj(number -> UnsolvedProblem.builder()
                                                                                          .number(number)
                                                                                          .tags(new HashSet<>())
                                                                                          .tier(Tier.BRONZE_IV)
                                                                                          .name(String.valueOf(number))
                                                                                          .build())
                                                       .toList();

        int start = 1000;

        problemRepository.saveAll(savedProblems);

        List<User> users = IntStream.range(1, 12)
                                    .mapToObj(name -> new User(String.valueOf(name), 10))
                                    .collect(Collectors.toList());

        when(userDetailRequest.getUserDetail()).thenReturn(users);

        for (User user : users) {
            List<UnsolvedProblem> newSolvedProblems = IntStream.range(start, start + 10)
                                                               .mapToObj(number -> UnsolvedProblem.builder()
                                                                                                  .number(number)
                                                                                                  .tags(new HashSet<>())
                                                                                                  .tier(Tier.BRONZE_IV)
                                                                                                  .name(String.valueOf(
                                                                                                          number))
                                                                                                  .build())
                                                               .collect(Collectors.toList());
            start+=10;
            when(problemRequestSolvedByUser.getProblems(user.getHandle())).thenReturn(newSolvedProblems);
        }

        launchJob(job.problemDeleteCheckJob(), null);

        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);
        long readCount = stepExecution.getReadCount();
        long writeCount = stepExecution.getWriteCount();



        int remainedSolvedProblemCount = problemRepository.findAll()
                                                          .size();

        Assertions.assertThat(readCount).isEqualTo(3);
        Assertions.assertThat(readCount).isEqualTo(3);

        Assertions.assertThat(remainedSolvedProblemCount)
                  .isEqualTo(90);

    }

    @Test
    void 트랜잭션_롤백_테스트() {

    }


}
