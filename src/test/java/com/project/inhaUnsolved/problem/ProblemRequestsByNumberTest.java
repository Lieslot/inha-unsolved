package com.project.inhaUnsolved.problem;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.service.ProblemRequestByNumber;
import java.util.List;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProblemRequestsByNumberTest {

    @Autowired
    ProblemRequestByNumber request;



    @Test
    void requestNewProblemTest() {
        try {
            List<UnsolvedProblem> newProblems = request.getNewProblems(31000);
            Assertions.assertThat((long) newProblems.size()).isEqualTo(100);
            System.out.println();
        }  catch (Exception e) {
            System.out.println();
        }




    }


}
