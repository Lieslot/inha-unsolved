package com.project.batch;


import com.project.batch.dto.NewUnsolvedProblems;
import com.project.batch.newproblemadd.NewProblemAddService;
import com.project.batch.newproblemadd.NewUnsolvedProblemWriter;
import com.project.batch.problemrenew.ProblemDetailRenewService;
import com.project.inhaUnsolved.domain.problem.domain.Tier;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.repository.SolvedProblemRepository;
import com.project.inhaUnsolved.service.ProblemService;
import com.zaxxer.hikari.HikariDataSource;
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
    private HikariDataSource dataSource;
    @Autowired
    private ProblemService problemService;
    @Autowired
    private NewProblemAddService newProblemAddService;
    @Autowired
    ProblemDetailRenewService problemDetailRenewService;
    @Autowired
    private ProblemRepository unsolvedProblemRepository;
    @Autowired
    private SolvedProblemRepository solvedProblemRepository;
    @Autowired
    private PlatformTransactionManager transactionManager;
    private List<UnsolvedProblem> test1 = new ArrayList<>();


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
            UnsolvedProblem testProblem = UnsolvedProblem.builder()
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
        NewUnsolvedProblemWriter newUnsolvedProblemWriter = new NewUnsolvedProblemWriter(newProblemAddService);

        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(2);

        List<Integer> numbers = test1.stream()
                                     .map(UnsolvedProblem::getNumber)
                                     .toList();

        NewUnsolvedProblems newUnsolvedProblems = new NewUnsolvedProblems(test1);

        addThread(() -> problemService.deleteAllUnsolvedProblemByNumbers(numbers), latch, executorService);
        addThread(() -> newUnsolvedProblemWriter.addProblems(newUnsolvedProblems), latch, executorService);

        latch.await();

        IntStream.range(startNumber, startNumber + testcaseCount)
                 .forEach((number) -> {
                             Assertions.assertThat(unsolvedProblemRepository.existsByNumber(number))
                                       .isFalse();
                         }
                 );


    }

    @Test
    void updateDeleteTest() throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        CountDownLatch latch = new CountDownLatch(2);

        List<UnsolvedProblem> problems = problemService.saveAllUnsolvedProblems(test1);

        List<Integer> problemNumbers = problems.stream()
                                               .map(UnsolvedProblem::getNumber)
                                               .toList();
        List<Integer> problemIds = problems.stream()
                                           .map(UnsolvedProblem::getId)
                                           .toList();

        addThread(() -> problemService.deleteAllUnsolvedProblemByNumbers(problemNumbers), latch, executorService);
        addThread(() -> problemDetailRenewService.renewProblemDetails(problemIds, problems), latch, executorService);

        latch.await();

        IntStream.range(startNumber, startNumber + testcaseCount)
                 .forEach((number) -> {
                             Assertions.assertThat(unsolvedProblemRepository.existsByNumber(number))
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
                                 unsolvedProblemRepository.deleteByNumber(number);
                                 solvedProblemRepository.deleteByNumber(number);
                             }
                     );
            return null;
        });


    }


}
