package com.project.batch;

import static org.mockito.Mockito.when;

import com.project.api.ProblemRequestSolvedByUser;
import com.project.api.UserDetailRequest;
import com.project.batch.solvecheck.ProblemSolveCheckJobConfig;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepository;
import com.project.inhaUnsolved.domain.user.User;
import jakarta.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.StepExecution;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;


public class ProblemDeleteCheckJobTest extends BatchTestSupport {

    @Autowired
    private ProblemSolveCheckJobConfig job;
    @Autowired
    private ProblemRepository problemRepository;
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
            start += 10;
            when(problemRequestSolvedByUser.getProblems(user.getHandle())).thenReturn(newSolvedProblems);
        }

        launchJob(job.problemSolveCheckJob(), null);

        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);
        long readCount = stepExecution.getReadCount();

        int remainedSolvedProblemCount = problemRepository.findAll()
                                                          .size();

        Assertions.assertThat(readCount)
                  .isEqualTo(11);
        Assertions.assertThat(remainedSolvedProblemCount)
                  .isEqualTo(90);

    }

    @Test
    void 기존_유저가_있을_경우_동작_테스트() throws Exception {
        List<UnsolvedProblem> savedProblems = IntStream.range(1000, 1200)
                                                       .mapToObj(number -> UnsolvedProblem.builder()
                                                                                          .number(number)
                                                                                          .tags(new HashSet<>())
                                                                                          .tier(Tier.BRONZE_IV)
                                                                                          .name(String.valueOf(number))
                                                                                          .build())
                                                       .toList();

        int start = 1000;

        saveAll(savedProblems);

        List<User> users = IntStream.range(1, 12)
                                    .mapToObj(name -> new User(String.valueOf(name), 10))
                                    .collect(Collectors.toList());
        List<User> existingUsers = IntStream.range(1, 5)
                                    .mapToObj(name -> new User(String.valueOf(name), 5))
                                    .collect(Collectors.toList());
        saveAll(existingUsers);

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
            start += 10;
            when(problemRequestSolvedByUser.getProblems(user.getHandle())).thenReturn(newSolvedProblems);
        }

        launchJob(job.problemSolveCheckJob(), null);

        StepExecution stepExecution = (StepExecution) ((List) jobExecution.getStepExecutions()).get(0);
        long readCount = stepExecution.getReadCount();

        int remainedSolvedProblemCount = problemRepository.findAll()
                                                          .size();

        Assertions.assertThat(readCount)
                  .isEqualTo(11);
        Assertions.assertThat(remainedSolvedProblemCount)
                  .isEqualTo(90);

        List<User> allUsers = new ArrayList<>();

        for (int i = 1; i <= 11; i++) {
            EntityManager entityManager1 = getEntityManager();
            User user = entityManager1.find(User.class, i);
            allUsers.add(user);
            Assertions.assertThat(user.getSolvingProblemCount())
                      .isEqualTo(10);
        }
        AtomicInteger handle = new AtomicInteger(1);

        allUsers.forEach(
          user -> {
              Assertions.assertThat(allUsers).anyMatch((user1) -> user1.getHandle()
                                                           .equals(String.valueOf(handle)));
              handle.incrementAndGet();
          }
        );
    }


}
