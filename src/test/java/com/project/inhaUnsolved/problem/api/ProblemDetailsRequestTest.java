package com.project.inhaUnsolved.problem.api;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.api.ProblemSolvedByUserRequest;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProblemDetailsRequestTest {



    @Autowired
    private ProblemSolvedByUserRequest request;


    @DisplayName("유저 정보 API 테스트")
    @Test
    void userDetailApiTest() {
        List<UnsolvedProblem> problems = request.getProblems("ditn258gh");
        System.out.println("problems = " + problems);


    }

}
