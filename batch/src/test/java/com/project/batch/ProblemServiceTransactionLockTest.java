package com.project.batch;


import com.project.batch.dto.NewUnsolvedProblems;
import com.project.batch.newproblemadd.NewProblemsAddService;
import com.project.batch.newproblemadd.NewProblemsWriter;
import com.project.batch.problemrenew.ProblemDetailRenewService;
import com.project.inhaUnsolved.domain.problem.domain.Problem;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.service.ProblemService;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;


@ActiveProfiles("test")
@SpringBootTest
public class ProblemServiceTransactionLockTest {

    int testcaseCount = 1000;
    int startNumber = 50000;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private NewProblemsAddService newProblemAddService;
    @Autowired
    ProblemDetailRenewService problemDetailRenewService;
    @Autowired
    private ProblemRepository problemRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private List<Problem> test1 = new ArrayList<>();


    @AfterEach
    void tearDown() {
        problemRepository.deleteAll();
    }


    public void addThread(Runnable logic, CountDownLatch latch, ExecutorService executorService) {

        executorService.submit(() -> {
            TransactionTemplate template = new TransactionTemplate(transactionManager);

            template.execute(status -> {
                try {
                    logic.run();
                } catch (Exception e) {
                    e.printStackTrace();
                    status.setRollbackOnly();
                }

                return null;
            });
            latch.countDown();
        });

    }


    @BeforeEach
    void setTestCase() {

        for (int i = startNumber; i < startNumber + testcaseCount; i++) {
            Problem testProblem = Problem.builder()
                                                         .name("test1")
                                                         .tags(new HashSet<>())
                                                         .number(i)
                                                         .tier(Tier.BRONZE_IV)
                                                         .build();
            test1.add(testProblem);
        }


    }


    @Test
    void addDeleteProblemLockTest() throws InterruptedException {
        NewProblemsWriter newUnsolvedProblemWriter = new NewProblemsWriter(newProblemAddService);

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(2);

        List<Integer> numbers = test1.stream()
                                     .map(Problem::getNumber)
                                     .toList();

        NewUnsolvedProblems newUnsolvedProblems = new NewUnsolvedProblems(test1);

        addThread(() -> problemService.changeToSolved(numbers), latch, executorService);
        addThread(() -> newUnsolvedProblemWriter.addProblems(newUnsolvedProblems), latch, executorService);

        latch.await();

        IntStream.range(startNumber, startNumber + testcaseCount)
                 .forEach((number) -> {
                             Assertions.assertThat(problemRepository.existsByNumber(number))
                                       .isFalse();
                         }
                 );


    }

    @Test
    void updateDeleteTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(2);

        List<Problem> problems = problemService.saveAllUnsolvedProblems(test1);

        List<Integer> problemNumbers = problems.stream()
                                               .map(Problem::getNumber)
                                               .toList();
        List<Integer> problemIds = problems.stream()
                                           .map(Problem::getId)
                                           .toList();

        addThread(() -> problemService.changeToSolved(problemNumbers), latch, executorService);
        addThread(() -> problemDetailRenewService.renewProblemDetails(problemIds, problems), latch, executorService);

        latch.await();

        IntStream.range(startNumber, startNumber + testcaseCount)
                 .forEach((number) -> {
                             Assertions.assertThat(problemRepository.existsByNumber(number))
                                       .isFalse();
                         }
                 );

    }


    @AfterEach
    void rollback() {
        TransactionTemplate template = new TransactionTemplate(transactionManager);

        template.execute(status -> {
            IntStream.range(startNumber, startNumber + testcaseCount)
                     .forEach((number) -> {
                                 problemRepository.deleteByNumber(number);
                             }
                     );
            return null;
        });


    }


}
