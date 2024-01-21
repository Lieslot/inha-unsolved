package com.project.inhaUnsolved.problem.service;

import com.project.inhaUnsolved.domain.problem.domain.UnsolvedProblem;
import com.project.inhaUnsolved.domain.problem.api.ProblemRequestByNumber;
import com.project.inhaUnsolved.domain.problem.service.ProblemService;
import com.project.inhaUnsolved.domain.problem.service.TagService;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class ProblemServiceTest {

    @Autowired
    ProblemService problemService;
    @Autowired
    ProblemRequestByNumber request;
    @Autowired
    TagService tagService;

    @BeforeEach
    void addTestCase() {

        List<String> problemNumbers = IntStream.range(1000,1100)
                                               .mapToObj(String::valueOf)
                                               .toList();
        List<UnsolvedProblem> newProblems = request.getProblemBy(problemNumbers);
        List<UnsolvedProblem> solvedProblem = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            solvedProblem.add(newProblems.get(i));
        }

        tagService.renewTagDetails();
        problemService.addProblems(newProblems);
        problemService.renewUnsolvedProblem(solvedProblem);

    }

    @Test
    void findLastNumberTest() {

        Integer lastSolvedNumber = problemService.findLastSolvedNumber();
        Integer lastUnsolvedNumber = problemService.findLastUnsolvedNumber();

        Assertions.assertThat(lastSolvedNumber).isEqualTo(1049);
        Assertions.assertThat(lastUnsolvedNumber).isEqualTo(1099);
    }
}
