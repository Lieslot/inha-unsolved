package com.project.inhaUnsolved.problem.api;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import java.util.List;
import java.util.stream.IntStream;
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

        List<String> problemNumbers= IntStream.range(31000, 31099)
                                              .mapToObj(String::valueOf)
                                              .toList();

        List<UnsolvedProblem> newProblems = request.getProblemBy(problemNumbers);
        Assertions.assertThat((long) newProblems.size()).isEqualTo(100);



    }


}
