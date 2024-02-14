package com.project.inhaUnsolved.problem.service;


import com.project.inhaUnsolved.domain.problem.api.ProblemRequestSolvedByUser;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.repository.ProblemRepository;
import com.project.inhaUnsolved.domain.problem.service.ProblemRenewService;
import com.project.inhaUnsolved.domain.problem.vo.NewSolvedProblemStore;
import com.project.inhaUnsolved.domain.user.User;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest

public class RenewUnsolvedProblemServiceTest {


    @Autowired
    private ProblemRenewService service;
    @Autowired
    private ProblemRepository problemRepository;

    @Autowired
    private ProblemRequestSolvedByUser request;


    @Transactional
    @Test
    void transactionTest() {

        long initCount = problemRepository.count();


        NewSolvedProblemStore problemStore = new NewSolvedProblemStore();
        List<User> userBuffer = new ArrayList<>();

        List<UnsolvedProblem> problems = new ArrayList<>();


        problemStore.storeSolvedProblems(problems);

        service.saveTransaction(problemStore.flushProblems(), userBuffer);



    }


    @Test
    void serviceTest() {
        service.renewUnsolvedProblem();
    }




}
