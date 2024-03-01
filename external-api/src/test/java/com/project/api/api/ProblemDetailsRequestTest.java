package com.project.api.api;

import com.project.api.ProblemRequestSolvedByUser;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProblemDetailsRequestTest {



    @Autowired
    private ProblemRequestSolvedByUser request;


    @DisplayName("유저 정보 API 테스트")
    @Test
    void userDetailApiTest() {
        List<UnsolvedProblem> problems = request.getProblems("ditn258gh");
        System.out.println("problems = " + problems);


    }

}
